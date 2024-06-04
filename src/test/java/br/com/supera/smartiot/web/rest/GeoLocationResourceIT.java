package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.GeoLocationAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.GeoLocation;
import br.com.supera.smartiot.repository.GeoLocationRepository;
import br.com.supera.smartiot.service.dto.GeoLocationDTO;
import br.com.supera.smartiot.service.mapper.GeoLocationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GeoLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeoLocationResourceIT {

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String DEFAULT_FULL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_FULL_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/geo-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GeoLocationRepository geoLocationRepository;

    @Autowired
    private GeoLocationMapper geoLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeoLocationMockMvc;

    private GeoLocation geoLocation;

    private GeoLocation insertedGeoLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoLocation createEntity(EntityManager em) {
        GeoLocation geoLocation = new GeoLocation()
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .fullAddress(DEFAULT_FULL_ADDRESS);
        return geoLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoLocation createUpdatedEntity(EntityManager em) {
        GeoLocation geoLocation = new GeoLocation()
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .fullAddress(UPDATED_FULL_ADDRESS);
        return geoLocation;
    }

    @BeforeEach
    public void initTest() {
        geoLocation = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGeoLocation != null) {
            geoLocationRepository.delete(insertedGeoLocation);
            insertedGeoLocation = null;
        }
    }

    @Test
    @Transactional
    void createGeoLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GeoLocation
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);
        var returnedGeoLocationDTO = om.readValue(
            restGeoLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoLocationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GeoLocationDTO.class
        );

        // Validate the GeoLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGeoLocation = geoLocationMapper.toEntity(returnedGeoLocationDTO);
        assertGeoLocationUpdatableFieldsEquals(returnedGeoLocation, getPersistedGeoLocation(returnedGeoLocation));

        insertedGeoLocation = returnedGeoLocation;
    }

    @Test
    @Transactional
    void createGeoLocationWithExistingId() throws Exception {
        // Create the GeoLocation with an existing ID
        geoLocation.setId(1L);
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeoLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeoLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        geoLocation.setLatitude(null);

        // Create the GeoLocation, which fails.
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        restGeoLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        geoLocation.setLongitude(null);

        // Create the GeoLocation, which fails.
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        restGeoLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGeoLocations() throws Exception {
        // Initialize the database
        insertedGeoLocation = geoLocationRepository.saveAndFlush(geoLocation);

        // Get all the geoLocationList
        restGeoLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geoLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullAddress").value(hasItem(DEFAULT_FULL_ADDRESS)));
    }

    @Test
    @Transactional
    void getGeoLocation() throws Exception {
        // Initialize the database
        insertedGeoLocation = geoLocationRepository.saveAndFlush(geoLocation);

        // Get the geoLocation
        restGeoLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, geoLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(geoLocation.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.fullAddress").value(DEFAULT_FULL_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingGeoLocation() throws Exception {
        // Get the geoLocation
        restGeoLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGeoLocation() throws Exception {
        // Initialize the database
        insertedGeoLocation = geoLocationRepository.saveAndFlush(geoLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoLocation
        GeoLocation updatedGeoLocation = geoLocationRepository.findById(geoLocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGeoLocation are not directly saved in db
        em.detach(updatedGeoLocation);
        updatedGeoLocation.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE).fullAddress(UPDATED_FULL_ADDRESS);
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(updatedGeoLocation);

        restGeoLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, geoLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geoLocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the GeoLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGeoLocationToMatchAllProperties(updatedGeoLocation);
    }

    @Test
    @Transactional
    void putNonExistingGeoLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoLocation.setId(longCount.incrementAndGet());

        // Create the GeoLocation
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, geoLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geoLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeoLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoLocation.setId(longCount.incrementAndGet());

        // Create the GeoLocation
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(geoLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeoLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoLocation.setId(longCount.incrementAndGet());

        // Create the GeoLocation
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(geoLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeoLocationWithPatch() throws Exception {
        // Initialize the database
        insertedGeoLocation = geoLocationRepository.saveAndFlush(geoLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoLocation using partial update
        GeoLocation partialUpdatedGeoLocation = new GeoLocation();
        partialUpdatedGeoLocation.setId(geoLocation.getId());

        partialUpdatedGeoLocation.longitude(UPDATED_LONGITUDE);

        restGeoLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoLocation))
            )
            .andExpect(status().isOk());

        // Validate the GeoLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGeoLocation, geoLocation),
            getPersistedGeoLocation(geoLocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateGeoLocationWithPatch() throws Exception {
        // Initialize the database
        insertedGeoLocation = geoLocationRepository.saveAndFlush(geoLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the geoLocation using partial update
        GeoLocation partialUpdatedGeoLocation = new GeoLocation();
        partialUpdatedGeoLocation.setId(geoLocation.getId());

        partialUpdatedGeoLocation.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE).fullAddress(UPDATED_FULL_ADDRESS);

        restGeoLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeoLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGeoLocation))
            )
            .andExpect(status().isOk());

        // Validate the GeoLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGeoLocationUpdatableFieldsEquals(partialUpdatedGeoLocation, getPersistedGeoLocation(partialUpdatedGeoLocation));
    }

    @Test
    @Transactional
    void patchNonExistingGeoLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoLocation.setId(longCount.incrementAndGet());

        // Create the GeoLocation
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeoLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, geoLocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeoLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoLocation.setId(longCount.incrementAndGet());

        // Create the GeoLocation
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(geoLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeoLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeoLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        geoLocation.setId(longCount.incrementAndGet());

        // Create the GeoLocation
        GeoLocationDTO geoLocationDTO = geoLocationMapper.toDto(geoLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeoLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(geoLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeoLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeoLocation() throws Exception {
        // Initialize the database
        insertedGeoLocation = geoLocationRepository.saveAndFlush(geoLocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the geoLocation
        restGeoLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, geoLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return geoLocationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected GeoLocation getPersistedGeoLocation(GeoLocation geoLocation) {
        return geoLocationRepository.findById(geoLocation.getId()).orElseThrow();
    }

    protected void assertPersistedGeoLocationToMatchAllProperties(GeoLocation expectedGeoLocation) {
        assertGeoLocationAllPropertiesEquals(expectedGeoLocation, getPersistedGeoLocation(expectedGeoLocation));
    }

    protected void assertPersistedGeoLocationToMatchUpdatableProperties(GeoLocation expectedGeoLocation) {
        assertGeoLocationAllUpdatablePropertiesEquals(expectedGeoLocation, getPersistedGeoLocation(expectedGeoLocation));
    }
}
