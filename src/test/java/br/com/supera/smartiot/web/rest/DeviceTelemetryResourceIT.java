package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.DeviceTelemetryAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.DeviceTelemetry;
import br.com.supera.smartiot.repository.DeviceTelemetryRepository;
import br.com.supera.smartiot.service.dto.DeviceTelemetryDTO;
import br.com.supera.smartiot.service.mapper.DeviceTelemetryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DeviceTelemetryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceTelemetryResourceIT {

    private static final Instant DEFAULT_TELEMETRY_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TELEMETRY_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Float DEFAULT_SPEED = 1F;
    private static final Float UPDATED_SPEED = 2F;

    private static final Float DEFAULT_FUEL_LEVEL = 1F;
    private static final Float UPDATED_FUEL_LEVEL = 2F;

    private static final String DEFAULT_ENGINE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/device-telemetries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeviceTelemetryRepository deviceTelemetryRepository;

    @Autowired
    private DeviceTelemetryMapper deviceTelemetryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceTelemetryMockMvc;

    private DeviceTelemetry deviceTelemetry;

    private DeviceTelemetry insertedDeviceTelemetry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceTelemetry createEntity(EntityManager em) {
        DeviceTelemetry deviceTelemetry = new DeviceTelemetry()
            .telemetryTimestamp(DEFAULT_TELEMETRY_TIMESTAMP)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .speed(DEFAULT_SPEED)
            .fuelLevel(DEFAULT_FUEL_LEVEL)
            .engineStatus(DEFAULT_ENGINE_STATUS);
        return deviceTelemetry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceTelemetry createUpdatedEntity(EntityManager em) {
        DeviceTelemetry deviceTelemetry = new DeviceTelemetry()
            .telemetryTimestamp(UPDATED_TELEMETRY_TIMESTAMP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speed(UPDATED_SPEED)
            .fuelLevel(UPDATED_FUEL_LEVEL)
            .engineStatus(UPDATED_ENGINE_STATUS);
        return deviceTelemetry;
    }

    @BeforeEach
    public void initTest() {
        deviceTelemetry = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDeviceTelemetry != null) {
            deviceTelemetryRepository.delete(insertedDeviceTelemetry);
            insertedDeviceTelemetry = null;
        }
    }

    @Test
    @Transactional
    void createDeviceTelemetry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DeviceTelemetry
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);
        var returnedDeviceTelemetryDTO = om.readValue(
            restDeviceTelemetryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceTelemetryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DeviceTelemetryDTO.class
        );

        // Validate the DeviceTelemetry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDeviceTelemetry = deviceTelemetryMapper.toEntity(returnedDeviceTelemetryDTO);
        assertDeviceTelemetryUpdatableFieldsEquals(returnedDeviceTelemetry, getPersistedDeviceTelemetry(returnedDeviceTelemetry));

        insertedDeviceTelemetry = returnedDeviceTelemetry;
    }

    @Test
    @Transactional
    void createDeviceTelemetryWithExistingId() throws Exception {
        // Create the DeviceTelemetry with an existing ID
        deviceTelemetry.setId(1L);
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceTelemetryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceTelemetryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceTelemetry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTelemetryTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deviceTelemetry.setTelemetryTimestamp(null);

        // Create the DeviceTelemetry, which fails.
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        restDeviceTelemetryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceTelemetryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deviceTelemetry.setLatitude(null);

        // Create the DeviceTelemetry, which fails.
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        restDeviceTelemetryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceTelemetryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deviceTelemetry.setLongitude(null);

        // Create the DeviceTelemetry, which fails.
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        restDeviceTelemetryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceTelemetryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeviceTelemetries() throws Exception {
        // Initialize the database
        insertedDeviceTelemetry = deviceTelemetryRepository.saveAndFlush(deviceTelemetry);

        // Get all the deviceTelemetryList
        restDeviceTelemetryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceTelemetry.getId().intValue())))
            .andExpect(jsonPath("$.[*].telemetryTimestamp").value(hasItem(DEFAULT_TELEMETRY_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].fuelLevel").value(hasItem(DEFAULT_FUEL_LEVEL.doubleValue())))
            .andExpect(jsonPath("$.[*].engineStatus").value(hasItem(DEFAULT_ENGINE_STATUS)));
    }

    @Test
    @Transactional
    void getDeviceTelemetry() throws Exception {
        // Initialize the database
        insertedDeviceTelemetry = deviceTelemetryRepository.saveAndFlush(deviceTelemetry);

        // Get the deviceTelemetry
        restDeviceTelemetryMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceTelemetry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceTelemetry.getId().intValue()))
            .andExpect(jsonPath("$.telemetryTimestamp").value(DEFAULT_TELEMETRY_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED.doubleValue()))
            .andExpect(jsonPath("$.fuelLevel").value(DEFAULT_FUEL_LEVEL.doubleValue()))
            .andExpect(jsonPath("$.engineStatus").value(DEFAULT_ENGINE_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingDeviceTelemetry() throws Exception {
        // Get the deviceTelemetry
        restDeviceTelemetryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeviceTelemetry() throws Exception {
        // Initialize the database
        insertedDeviceTelemetry = deviceTelemetryRepository.saveAndFlush(deviceTelemetry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceTelemetry
        DeviceTelemetry updatedDeviceTelemetry = deviceTelemetryRepository.findById(deviceTelemetry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeviceTelemetry are not directly saved in db
        em.detach(updatedDeviceTelemetry);
        updatedDeviceTelemetry
            .telemetryTimestamp(UPDATED_TELEMETRY_TIMESTAMP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speed(UPDATED_SPEED)
            .fuelLevel(UPDATED_FUEL_LEVEL)
            .engineStatus(UPDATED_ENGINE_STATUS);
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(updatedDeviceTelemetry);

        restDeviceTelemetryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceTelemetryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceTelemetryDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceTelemetry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeviceTelemetryToMatchAllProperties(updatedDeviceTelemetry);
    }

    @Test
    @Transactional
    void putNonExistingDeviceTelemetry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceTelemetry.setId(longCount.incrementAndGet());

        // Create the DeviceTelemetry
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceTelemetryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceTelemetryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceTelemetryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceTelemetry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceTelemetry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceTelemetry.setId(longCount.incrementAndGet());

        // Create the DeviceTelemetry
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTelemetryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceTelemetryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceTelemetry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceTelemetry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceTelemetry.setId(longCount.incrementAndGet());

        // Create the DeviceTelemetry
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTelemetryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceTelemetryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceTelemetry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceTelemetryWithPatch() throws Exception {
        // Initialize the database
        insertedDeviceTelemetry = deviceTelemetryRepository.saveAndFlush(deviceTelemetry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceTelemetry using partial update
        DeviceTelemetry partialUpdatedDeviceTelemetry = new DeviceTelemetry();
        partialUpdatedDeviceTelemetry.setId(deviceTelemetry.getId());

        partialUpdatedDeviceTelemetry.speed(UPDATED_SPEED);

        restDeviceTelemetryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceTelemetry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeviceTelemetry))
            )
            .andExpect(status().isOk());

        // Validate the DeviceTelemetry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceTelemetryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDeviceTelemetry, deviceTelemetry),
            getPersistedDeviceTelemetry(deviceTelemetry)
        );
    }

    @Test
    @Transactional
    void fullUpdateDeviceTelemetryWithPatch() throws Exception {
        // Initialize the database
        insertedDeviceTelemetry = deviceTelemetryRepository.saveAndFlush(deviceTelemetry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceTelemetry using partial update
        DeviceTelemetry partialUpdatedDeviceTelemetry = new DeviceTelemetry();
        partialUpdatedDeviceTelemetry.setId(deviceTelemetry.getId());

        partialUpdatedDeviceTelemetry
            .telemetryTimestamp(UPDATED_TELEMETRY_TIMESTAMP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speed(UPDATED_SPEED)
            .fuelLevel(UPDATED_FUEL_LEVEL)
            .engineStatus(UPDATED_ENGINE_STATUS);

        restDeviceTelemetryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceTelemetry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeviceTelemetry))
            )
            .andExpect(status().isOk());

        // Validate the DeviceTelemetry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceTelemetryUpdatableFieldsEquals(
            partialUpdatedDeviceTelemetry,
            getPersistedDeviceTelemetry(partialUpdatedDeviceTelemetry)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDeviceTelemetry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceTelemetry.setId(longCount.incrementAndGet());

        // Create the DeviceTelemetry
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceTelemetryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceTelemetryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceTelemetryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceTelemetry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceTelemetry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceTelemetry.setId(longCount.incrementAndGet());

        // Create the DeviceTelemetry
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTelemetryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceTelemetryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceTelemetry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceTelemetry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceTelemetry.setId(longCount.incrementAndGet());

        // Create the DeviceTelemetry
        DeviceTelemetryDTO deviceTelemetryDTO = deviceTelemetryMapper.toDto(deviceTelemetry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceTelemetryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(deviceTelemetryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceTelemetry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceTelemetry() throws Exception {
        // Initialize the database
        insertedDeviceTelemetry = deviceTelemetryRepository.saveAndFlush(deviceTelemetry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the deviceTelemetry
        restDeviceTelemetryMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceTelemetry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return deviceTelemetryRepository.count();
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

    protected DeviceTelemetry getPersistedDeviceTelemetry(DeviceTelemetry deviceTelemetry) {
        return deviceTelemetryRepository.findById(deviceTelemetry.getId()).orElseThrow();
    }

    protected void assertPersistedDeviceTelemetryToMatchAllProperties(DeviceTelemetry expectedDeviceTelemetry) {
        assertDeviceTelemetryAllPropertiesEquals(expectedDeviceTelemetry, getPersistedDeviceTelemetry(expectedDeviceTelemetry));
    }

    protected void assertPersistedDeviceTelemetryToMatchUpdatableProperties(DeviceTelemetry expectedDeviceTelemetry) {
        assertDeviceTelemetryAllUpdatablePropertiesEquals(expectedDeviceTelemetry, getPersistedDeviceTelemetry(expectedDeviceTelemetry));
    }
}
