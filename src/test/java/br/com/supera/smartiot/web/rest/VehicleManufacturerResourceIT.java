package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.VehicleManufacturerAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.VehicleManufacturer;
import br.com.supera.smartiot.repository.VehicleManufacturerRepository;
import br.com.supera.smartiot.service.dto.VehicleManufacturerDTO;
import br.com.supera.smartiot.service.mapper.VehicleManufacturerMapper;
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
 * Integration tests for the {@link VehicleManufacturerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleManufacturerResourceIT {

    private static final String DEFAULT_MANUFACTURER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURER_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-manufacturers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleManufacturerRepository vehicleManufacturerRepository;

    @Autowired
    private VehicleManufacturerMapper vehicleManufacturerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleManufacturerMockMvc;

    private VehicleManufacturer vehicleManufacturer;

    private VehicleManufacturer insertedVehicleManufacturer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleManufacturer createEntity(EntityManager em) {
        VehicleManufacturer vehicleManufacturer = new VehicleManufacturer()
            .manufacturerName(DEFAULT_MANUFACTURER_NAME)
            .manufacturerCountry(DEFAULT_MANUFACTURER_COUNTRY);
        return vehicleManufacturer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleManufacturer createUpdatedEntity(EntityManager em) {
        VehicleManufacturer vehicleManufacturer = new VehicleManufacturer()
            .manufacturerName(UPDATED_MANUFACTURER_NAME)
            .manufacturerCountry(UPDATED_MANUFACTURER_COUNTRY);
        return vehicleManufacturer;
    }

    @BeforeEach
    public void initTest() {
        vehicleManufacturer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleManufacturer != null) {
            vehicleManufacturerRepository.delete(insertedVehicleManufacturer);
            insertedVehicleManufacturer = null;
        }
    }

    @Test
    @Transactional
    void createVehicleManufacturer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleManufacturer
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);
        var returnedVehicleManufacturerDTO = om.readValue(
            restVehicleManufacturerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleManufacturerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleManufacturerDTO.class
        );

        // Validate the VehicleManufacturer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleManufacturer = vehicleManufacturerMapper.toEntity(returnedVehicleManufacturerDTO);
        assertVehicleManufacturerUpdatableFieldsEquals(
            returnedVehicleManufacturer,
            getPersistedVehicleManufacturer(returnedVehicleManufacturer)
        );

        insertedVehicleManufacturer = returnedVehicleManufacturer;
    }

    @Test
    @Transactional
    void createVehicleManufacturerWithExistingId() throws Exception {
        // Create the VehicleManufacturer with an existing ID
        vehicleManufacturer.setId(1L);
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleManufacturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleManufacturerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleManufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkManufacturerNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleManufacturer.setManufacturerName(null);

        // Create the VehicleManufacturer, which fails.
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        restVehicleManufacturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleManufacturerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkManufacturerCountryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleManufacturer.setManufacturerCountry(null);

        // Create the VehicleManufacturer, which fails.
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        restVehicleManufacturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleManufacturerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleManufacturers() throws Exception {
        // Initialize the database
        insertedVehicleManufacturer = vehicleManufacturerRepository.saveAndFlush(vehicleManufacturer);

        // Get all the vehicleManufacturerList
        restVehicleManufacturerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleManufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].manufacturerName").value(hasItem(DEFAULT_MANUFACTURER_NAME)))
            .andExpect(jsonPath("$.[*].manufacturerCountry").value(hasItem(DEFAULT_MANUFACTURER_COUNTRY)));
    }

    @Test
    @Transactional
    void getVehicleManufacturer() throws Exception {
        // Initialize the database
        insertedVehicleManufacturer = vehicleManufacturerRepository.saveAndFlush(vehicleManufacturer);

        // Get the vehicleManufacturer
        restVehicleManufacturerMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleManufacturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleManufacturer.getId().intValue()))
            .andExpect(jsonPath("$.manufacturerName").value(DEFAULT_MANUFACTURER_NAME))
            .andExpect(jsonPath("$.manufacturerCountry").value(DEFAULT_MANUFACTURER_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingVehicleManufacturer() throws Exception {
        // Get the vehicleManufacturer
        restVehicleManufacturerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleManufacturer() throws Exception {
        // Initialize the database
        insertedVehicleManufacturer = vehicleManufacturerRepository.saveAndFlush(vehicleManufacturer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleManufacturer
        VehicleManufacturer updatedVehicleManufacturer = vehicleManufacturerRepository.findById(vehicleManufacturer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleManufacturer are not directly saved in db
        em.detach(updatedVehicleManufacturer);
        updatedVehicleManufacturer.manufacturerName(UPDATED_MANUFACTURER_NAME).manufacturerCountry(UPDATED_MANUFACTURER_COUNTRY);
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(updatedVehicleManufacturer);

        restVehicleManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleManufacturerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleManufacturerDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleManufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleManufacturerToMatchAllProperties(updatedVehicleManufacturer);
    }

    @Test
    @Transactional
    void putNonExistingVehicleManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleManufacturer.setId(longCount.incrementAndGet());

        // Create the VehicleManufacturer
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleManufacturerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleManufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleManufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleManufacturer.setId(longCount.incrementAndGet());

        // Create the VehicleManufacturer
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleManufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleManufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleManufacturer.setId(longCount.incrementAndGet());

        // Create the VehicleManufacturer
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleManufacturerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleManufacturerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleManufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleManufacturerWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleManufacturer = vehicleManufacturerRepository.saveAndFlush(vehicleManufacturer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleManufacturer using partial update
        VehicleManufacturer partialUpdatedVehicleManufacturer = new VehicleManufacturer();
        partialUpdatedVehicleManufacturer.setId(vehicleManufacturer.getId());

        partialUpdatedVehicleManufacturer.manufacturerName(UPDATED_MANUFACTURER_NAME).manufacturerCountry(UPDATED_MANUFACTURER_COUNTRY);

        restVehicleManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleManufacturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleManufacturer))
            )
            .andExpect(status().isOk());

        // Validate the VehicleManufacturer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleManufacturerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleManufacturer, vehicleManufacturer),
            getPersistedVehicleManufacturer(vehicleManufacturer)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleManufacturerWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleManufacturer = vehicleManufacturerRepository.saveAndFlush(vehicleManufacturer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleManufacturer using partial update
        VehicleManufacturer partialUpdatedVehicleManufacturer = new VehicleManufacturer();
        partialUpdatedVehicleManufacturer.setId(vehicleManufacturer.getId());

        partialUpdatedVehicleManufacturer.manufacturerName(UPDATED_MANUFACTURER_NAME).manufacturerCountry(UPDATED_MANUFACTURER_COUNTRY);

        restVehicleManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleManufacturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleManufacturer))
            )
            .andExpect(status().isOk());

        // Validate the VehicleManufacturer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleManufacturerUpdatableFieldsEquals(
            partialUpdatedVehicleManufacturer,
            getPersistedVehicleManufacturer(partialUpdatedVehicleManufacturer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVehicleManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleManufacturer.setId(longCount.incrementAndGet());

        // Create the VehicleManufacturer
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleManufacturerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleManufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleManufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleManufacturer.setId(longCount.incrementAndGet());

        // Create the VehicleManufacturer
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleManufacturerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleManufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleManufacturer.setId(longCount.incrementAndGet());

        // Create the VehicleManufacturer
        VehicleManufacturerDTO vehicleManufacturerDTO = vehicleManufacturerMapper.toDto(vehicleManufacturer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleManufacturerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleManufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleManufacturer() throws Exception {
        // Initialize the database
        insertedVehicleManufacturer = vehicleManufacturerRepository.saveAndFlush(vehicleManufacturer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleManufacturer
        restVehicleManufacturerMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleManufacturer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleManufacturerRepository.count();
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

    protected VehicleManufacturer getPersistedVehicleManufacturer(VehicleManufacturer vehicleManufacturer) {
        return vehicleManufacturerRepository.findById(vehicleManufacturer.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleManufacturerToMatchAllProperties(VehicleManufacturer expectedVehicleManufacturer) {
        assertVehicleManufacturerAllPropertiesEquals(
            expectedVehicleManufacturer,
            getPersistedVehicleManufacturer(expectedVehicleManufacturer)
        );
    }

    protected void assertPersistedVehicleManufacturerToMatchUpdatableProperties(VehicleManufacturer expectedVehicleManufacturer) {
        assertVehicleManufacturerAllUpdatablePropertiesEquals(
            expectedVehicleManufacturer,
            getPersistedVehicleManufacturer(expectedVehicleManufacturer)
        );
    }
}
