package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.WaterSensorAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.WaterSensor;
import br.com.supera.smartiot.domain.enumeration.SensorStatus;
import br.com.supera.smartiot.repository.WaterSensorRepository;
import br.com.supera.smartiot.service.dto.WaterSensorDTO;
import br.com.supera.smartiot.service.mapper.WaterSensorMapper;
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
 * Integration tests for the {@link WaterSensorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WaterSensorResourceIT {

    private static final String DEFAULT_SENSOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_SENSOR_ID = "BBBBBBBBBB";

    private static final SensorStatus DEFAULT_SENSOR_STATUS = SensorStatus.ACTIVE;
    private static final SensorStatus UPDATED_SENSOR_STATUS = SensorStatus.INACTIVE;

    private static final String ENTITY_API_URL = "/api/water-sensors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WaterSensorRepository waterSensorRepository;

    @Autowired
    private WaterSensorMapper waterSensorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWaterSensorMockMvc;

    private WaterSensor waterSensor;

    private WaterSensor insertedWaterSensor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaterSensor createEntity(EntityManager em) {
        WaterSensor waterSensor = new WaterSensor().sensorId(DEFAULT_SENSOR_ID).sensorStatus(DEFAULT_SENSOR_STATUS);
        return waterSensor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaterSensor createUpdatedEntity(EntityManager em) {
        WaterSensor waterSensor = new WaterSensor().sensorId(UPDATED_SENSOR_ID).sensorStatus(UPDATED_SENSOR_STATUS);
        return waterSensor;
    }

    @BeforeEach
    public void initTest() {
        waterSensor = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedWaterSensor != null) {
            waterSensorRepository.delete(insertedWaterSensor);
            insertedWaterSensor = null;
        }
    }

    @Test
    @Transactional
    void createWaterSensor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WaterSensor
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);
        var returnedWaterSensorDTO = om.readValue(
            restWaterSensorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterSensorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WaterSensorDTO.class
        );

        // Validate the WaterSensor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWaterSensor = waterSensorMapper.toEntity(returnedWaterSensorDTO);
        assertWaterSensorUpdatableFieldsEquals(returnedWaterSensor, getPersistedWaterSensor(returnedWaterSensor));

        insertedWaterSensor = returnedWaterSensor;
    }

    @Test
    @Transactional
    void createWaterSensorWithExistingId() throws Exception {
        // Create the WaterSensor with an existing ID
        waterSensor.setId(1L);
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaterSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterSensorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WaterSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSensorIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterSensor.setSensorId(null);

        // Create the WaterSensor, which fails.
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        restWaterSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterSensorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSensorStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterSensor.setSensorStatus(null);

        // Create the WaterSensor, which fails.
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        restWaterSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterSensorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWaterSensors() throws Exception {
        // Initialize the database
        insertedWaterSensor = waterSensorRepository.saveAndFlush(waterSensor);

        // Get all the waterSensorList
        restWaterSensorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waterSensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].sensorId").value(hasItem(DEFAULT_SENSOR_ID)))
            .andExpect(jsonPath("$.[*].sensorStatus").value(hasItem(DEFAULT_SENSOR_STATUS.toString())));
    }

    @Test
    @Transactional
    void getWaterSensor() throws Exception {
        // Initialize the database
        insertedWaterSensor = waterSensorRepository.saveAndFlush(waterSensor);

        // Get the waterSensor
        restWaterSensorMockMvc
            .perform(get(ENTITY_API_URL_ID, waterSensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waterSensor.getId().intValue()))
            .andExpect(jsonPath("$.sensorId").value(DEFAULT_SENSOR_ID))
            .andExpect(jsonPath("$.sensorStatus").value(DEFAULT_SENSOR_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWaterSensor() throws Exception {
        // Get the waterSensor
        restWaterSensorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWaterSensor() throws Exception {
        // Initialize the database
        insertedWaterSensor = waterSensorRepository.saveAndFlush(waterSensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterSensor
        WaterSensor updatedWaterSensor = waterSensorRepository.findById(waterSensor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWaterSensor are not directly saved in db
        em.detach(updatedWaterSensor);
        updatedWaterSensor.sensorId(UPDATED_SENSOR_ID).sensorStatus(UPDATED_SENSOR_STATUS);
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(updatedWaterSensor);

        restWaterSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waterSensorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterSensorDTO))
            )
            .andExpect(status().isOk());

        // Validate the WaterSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWaterSensorToMatchAllProperties(updatedWaterSensor);
    }

    @Test
    @Transactional
    void putNonExistingWaterSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterSensor.setId(longCount.incrementAndGet());

        // Create the WaterSensor
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaterSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waterSensorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterSensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWaterSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterSensor.setId(longCount.incrementAndGet());

        // Create the WaterSensor
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterSensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWaterSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterSensor.setId(longCount.incrementAndGet());

        // Create the WaterSensor
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterSensorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterSensorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaterSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWaterSensorWithPatch() throws Exception {
        // Initialize the database
        insertedWaterSensor = waterSensorRepository.saveAndFlush(waterSensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterSensor using partial update
        WaterSensor partialUpdatedWaterSensor = new WaterSensor();
        partialUpdatedWaterSensor.setId(waterSensor.getId());

        partialUpdatedWaterSensor.sensorId(UPDATED_SENSOR_ID);

        restWaterSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaterSensor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWaterSensor))
            )
            .andExpect(status().isOk());

        // Validate the WaterSensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWaterSensorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWaterSensor, waterSensor),
            getPersistedWaterSensor(waterSensor)
        );
    }

    @Test
    @Transactional
    void fullUpdateWaterSensorWithPatch() throws Exception {
        // Initialize the database
        insertedWaterSensor = waterSensorRepository.saveAndFlush(waterSensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterSensor using partial update
        WaterSensor partialUpdatedWaterSensor = new WaterSensor();
        partialUpdatedWaterSensor.setId(waterSensor.getId());

        partialUpdatedWaterSensor.sensorId(UPDATED_SENSOR_ID).sensorStatus(UPDATED_SENSOR_STATUS);

        restWaterSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaterSensor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWaterSensor))
            )
            .andExpect(status().isOk());

        // Validate the WaterSensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWaterSensorUpdatableFieldsEquals(partialUpdatedWaterSensor, getPersistedWaterSensor(partialUpdatedWaterSensor));
    }

    @Test
    @Transactional
    void patchNonExistingWaterSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterSensor.setId(longCount.incrementAndGet());

        // Create the WaterSensor
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaterSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, waterSensorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(waterSensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWaterSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterSensor.setId(longCount.incrementAndGet());

        // Create the WaterSensor
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(waterSensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWaterSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterSensor.setId(longCount.incrementAndGet());

        // Create the WaterSensor
        WaterSensorDTO waterSensorDTO = waterSensorMapper.toDto(waterSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterSensorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(waterSensorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaterSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWaterSensor() throws Exception {
        // Initialize the database
        insertedWaterSensor = waterSensorRepository.saveAndFlush(waterSensor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the waterSensor
        restWaterSensorMockMvc
            .perform(delete(ENTITY_API_URL_ID, waterSensor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return waterSensorRepository.count();
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

    protected WaterSensor getPersistedWaterSensor(WaterSensor waterSensor) {
        return waterSensorRepository.findById(waterSensor.getId()).orElseThrow();
    }

    protected void assertPersistedWaterSensorToMatchAllProperties(WaterSensor expectedWaterSensor) {
        assertWaterSensorAllPropertiesEquals(expectedWaterSensor, getPersistedWaterSensor(expectedWaterSensor));
    }

    protected void assertPersistedWaterSensorToMatchUpdatableProperties(WaterSensor expectedWaterSensor) {
        assertWaterSensorAllUpdatablePropertiesEquals(expectedWaterSensor, getPersistedWaterSensor(expectedWaterSensor));
    }
}
