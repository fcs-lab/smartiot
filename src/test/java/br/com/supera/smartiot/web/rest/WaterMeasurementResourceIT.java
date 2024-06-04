package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.WaterMeasurementAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.WaterMeasurement;
import br.com.supera.smartiot.repository.WaterMeasurementRepository;
import br.com.supera.smartiot.service.dto.WaterMeasurementDTO;
import br.com.supera.smartiot.service.mapper.WaterMeasurementMapper;
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
 * Integration tests for the {@link WaterMeasurementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WaterMeasurementResourceIT {

    private static final Instant DEFAULT_MEASUREMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MEASUREMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_WATER_LEVEL = 1F;
    private static final Float UPDATED_WATER_LEVEL = 2F;

    private static final String DEFAULT_WATER_QUALITY = "AAAAAAAAAA";
    private static final String UPDATED_WATER_QUALITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/water-measurements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WaterMeasurementRepository waterMeasurementRepository;

    @Autowired
    private WaterMeasurementMapper waterMeasurementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWaterMeasurementMockMvc;

    private WaterMeasurement waterMeasurement;

    private WaterMeasurement insertedWaterMeasurement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaterMeasurement createEntity(EntityManager em) {
        WaterMeasurement waterMeasurement = new WaterMeasurement()
            .measurementDate(DEFAULT_MEASUREMENT_DATE)
            .waterLevel(DEFAULT_WATER_LEVEL)
            .waterQuality(DEFAULT_WATER_QUALITY);
        return waterMeasurement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaterMeasurement createUpdatedEntity(EntityManager em) {
        WaterMeasurement waterMeasurement = new WaterMeasurement()
            .measurementDate(UPDATED_MEASUREMENT_DATE)
            .waterLevel(UPDATED_WATER_LEVEL)
            .waterQuality(UPDATED_WATER_QUALITY);
        return waterMeasurement;
    }

    @BeforeEach
    public void initTest() {
        waterMeasurement = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedWaterMeasurement != null) {
            waterMeasurementRepository.delete(insertedWaterMeasurement);
            insertedWaterMeasurement = null;
        }
    }

    @Test
    @Transactional
    void createWaterMeasurement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WaterMeasurement
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);
        var returnedWaterMeasurementDTO = om.readValue(
            restWaterMeasurementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterMeasurementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WaterMeasurementDTO.class
        );

        // Validate the WaterMeasurement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWaterMeasurement = waterMeasurementMapper.toEntity(returnedWaterMeasurementDTO);
        assertWaterMeasurementUpdatableFieldsEquals(returnedWaterMeasurement, getPersistedWaterMeasurement(returnedWaterMeasurement));

        insertedWaterMeasurement = returnedWaterMeasurement;
    }

    @Test
    @Transactional
    void createWaterMeasurementWithExistingId() throws Exception {
        // Create the WaterMeasurement with an existing ID
        waterMeasurement.setId(1L);
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaterMeasurementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterMeasurementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WaterMeasurement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMeasurementDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterMeasurement.setMeasurementDate(null);

        // Create the WaterMeasurement, which fails.
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        restWaterMeasurementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterMeasurementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWaterLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterMeasurement.setWaterLevel(null);

        // Create the WaterMeasurement, which fails.
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        restWaterMeasurementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterMeasurementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWaterMeasurements() throws Exception {
        // Initialize the database
        insertedWaterMeasurement = waterMeasurementRepository.saveAndFlush(waterMeasurement);

        // Get all the waterMeasurementList
        restWaterMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waterMeasurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].measurementDate").value(hasItem(DEFAULT_MEASUREMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].waterLevel").value(hasItem(DEFAULT_WATER_LEVEL.doubleValue())))
            .andExpect(jsonPath("$.[*].waterQuality").value(hasItem(DEFAULT_WATER_QUALITY)));
    }

    @Test
    @Transactional
    void getWaterMeasurement() throws Exception {
        // Initialize the database
        insertedWaterMeasurement = waterMeasurementRepository.saveAndFlush(waterMeasurement);

        // Get the waterMeasurement
        restWaterMeasurementMockMvc
            .perform(get(ENTITY_API_URL_ID, waterMeasurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waterMeasurement.getId().intValue()))
            .andExpect(jsonPath("$.measurementDate").value(DEFAULT_MEASUREMENT_DATE.toString()))
            .andExpect(jsonPath("$.waterLevel").value(DEFAULT_WATER_LEVEL.doubleValue()))
            .andExpect(jsonPath("$.waterQuality").value(DEFAULT_WATER_QUALITY));
    }

    @Test
    @Transactional
    void getNonExistingWaterMeasurement() throws Exception {
        // Get the waterMeasurement
        restWaterMeasurementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWaterMeasurement() throws Exception {
        // Initialize the database
        insertedWaterMeasurement = waterMeasurementRepository.saveAndFlush(waterMeasurement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterMeasurement
        WaterMeasurement updatedWaterMeasurement = waterMeasurementRepository.findById(waterMeasurement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWaterMeasurement are not directly saved in db
        em.detach(updatedWaterMeasurement);
        updatedWaterMeasurement
            .measurementDate(UPDATED_MEASUREMENT_DATE)
            .waterLevel(UPDATED_WATER_LEVEL)
            .waterQuality(UPDATED_WATER_QUALITY);
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(updatedWaterMeasurement);

        restWaterMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waterMeasurementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterMeasurementDTO))
            )
            .andExpect(status().isOk());

        // Validate the WaterMeasurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWaterMeasurementToMatchAllProperties(updatedWaterMeasurement);
    }

    @Test
    @Transactional
    void putNonExistingWaterMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterMeasurement.setId(longCount.incrementAndGet());

        // Create the WaterMeasurement
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaterMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waterMeasurementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterMeasurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWaterMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterMeasurement.setId(longCount.incrementAndGet());

        // Create the WaterMeasurement
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterMeasurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWaterMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterMeasurement.setId(longCount.incrementAndGet());

        // Create the WaterMeasurement
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterMeasurementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterMeasurementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaterMeasurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWaterMeasurementWithPatch() throws Exception {
        // Initialize the database
        insertedWaterMeasurement = waterMeasurementRepository.saveAndFlush(waterMeasurement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterMeasurement using partial update
        WaterMeasurement partialUpdatedWaterMeasurement = new WaterMeasurement();
        partialUpdatedWaterMeasurement.setId(waterMeasurement.getId());

        partialUpdatedWaterMeasurement.waterLevel(UPDATED_WATER_LEVEL);

        restWaterMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaterMeasurement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWaterMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the WaterMeasurement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWaterMeasurementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWaterMeasurement, waterMeasurement),
            getPersistedWaterMeasurement(waterMeasurement)
        );
    }

    @Test
    @Transactional
    void fullUpdateWaterMeasurementWithPatch() throws Exception {
        // Initialize the database
        insertedWaterMeasurement = waterMeasurementRepository.saveAndFlush(waterMeasurement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterMeasurement using partial update
        WaterMeasurement partialUpdatedWaterMeasurement = new WaterMeasurement();
        partialUpdatedWaterMeasurement.setId(waterMeasurement.getId());

        partialUpdatedWaterMeasurement
            .measurementDate(UPDATED_MEASUREMENT_DATE)
            .waterLevel(UPDATED_WATER_LEVEL)
            .waterQuality(UPDATED_WATER_QUALITY);

        restWaterMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaterMeasurement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWaterMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the WaterMeasurement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWaterMeasurementUpdatableFieldsEquals(
            partialUpdatedWaterMeasurement,
            getPersistedWaterMeasurement(partialUpdatedWaterMeasurement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingWaterMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterMeasurement.setId(longCount.incrementAndGet());

        // Create the WaterMeasurement
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaterMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, waterMeasurementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(waterMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterMeasurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWaterMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterMeasurement.setId(longCount.incrementAndGet());

        // Create the WaterMeasurement
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(waterMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterMeasurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWaterMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterMeasurement.setId(longCount.incrementAndGet());

        // Create the WaterMeasurement
        WaterMeasurementDTO waterMeasurementDTO = waterMeasurementMapper.toDto(waterMeasurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterMeasurementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(waterMeasurementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaterMeasurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWaterMeasurement() throws Exception {
        // Initialize the database
        insertedWaterMeasurement = waterMeasurementRepository.saveAndFlush(waterMeasurement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the waterMeasurement
        restWaterMeasurementMockMvc
            .perform(delete(ENTITY_API_URL_ID, waterMeasurement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return waterMeasurementRepository.count();
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

    protected WaterMeasurement getPersistedWaterMeasurement(WaterMeasurement waterMeasurement) {
        return waterMeasurementRepository.findById(waterMeasurement.getId()).orElseThrow();
    }

    protected void assertPersistedWaterMeasurementToMatchAllProperties(WaterMeasurement expectedWaterMeasurement) {
        assertWaterMeasurementAllPropertiesEquals(expectedWaterMeasurement, getPersistedWaterMeasurement(expectedWaterMeasurement));
    }

    protected void assertPersistedWaterMeasurementToMatchUpdatableProperties(WaterMeasurement expectedWaterMeasurement) {
        assertWaterMeasurementAllUpdatablePropertiesEquals(
            expectedWaterMeasurement,
            getPersistedWaterMeasurement(expectedWaterMeasurement)
        );
    }
}
