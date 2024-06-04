package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.WaterUsageLogAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.WaterUsageLog;
import br.com.supera.smartiot.repository.WaterUsageLogRepository;
import br.com.supera.smartiot.service.dto.WaterUsageLogDTO;
import br.com.supera.smartiot.service.mapper.WaterUsageLogMapper;
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
 * Integration tests for the {@link WaterUsageLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WaterUsageLogResourceIT {

    private static final Instant DEFAULT_USAGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_USAGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_AMOUNT_USED = 1F;
    private static final Float UPDATED_AMOUNT_USED = 2F;

    private static final String ENTITY_API_URL = "/api/water-usage-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WaterUsageLogRepository waterUsageLogRepository;

    @Autowired
    private WaterUsageLogMapper waterUsageLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWaterUsageLogMockMvc;

    private WaterUsageLog waterUsageLog;

    private WaterUsageLog insertedWaterUsageLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaterUsageLog createEntity(EntityManager em) {
        WaterUsageLog waterUsageLog = new WaterUsageLog().usageDate(DEFAULT_USAGE_DATE).amountUsed(DEFAULT_AMOUNT_USED);
        return waterUsageLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaterUsageLog createUpdatedEntity(EntityManager em) {
        WaterUsageLog waterUsageLog = new WaterUsageLog().usageDate(UPDATED_USAGE_DATE).amountUsed(UPDATED_AMOUNT_USED);
        return waterUsageLog;
    }

    @BeforeEach
    public void initTest() {
        waterUsageLog = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedWaterUsageLog != null) {
            waterUsageLogRepository.delete(insertedWaterUsageLog);
            insertedWaterUsageLog = null;
        }
    }

    @Test
    @Transactional
    void createWaterUsageLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WaterUsageLog
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);
        var returnedWaterUsageLogDTO = om.readValue(
            restWaterUsageLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterUsageLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WaterUsageLogDTO.class
        );

        // Validate the WaterUsageLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWaterUsageLog = waterUsageLogMapper.toEntity(returnedWaterUsageLogDTO);
        assertWaterUsageLogUpdatableFieldsEquals(returnedWaterUsageLog, getPersistedWaterUsageLog(returnedWaterUsageLog));

        insertedWaterUsageLog = returnedWaterUsageLog;
    }

    @Test
    @Transactional
    void createWaterUsageLogWithExistingId() throws Exception {
        // Create the WaterUsageLog with an existing ID
        waterUsageLog.setId(1L);
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaterUsageLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterUsageLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WaterUsageLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsageDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterUsageLog.setUsageDate(null);

        // Create the WaterUsageLog, which fails.
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        restWaterUsageLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterUsageLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountUsedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterUsageLog.setAmountUsed(null);

        // Create the WaterUsageLog, which fails.
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        restWaterUsageLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterUsageLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWaterUsageLogs() throws Exception {
        // Initialize the database
        insertedWaterUsageLog = waterUsageLogRepository.saveAndFlush(waterUsageLog);

        // Get all the waterUsageLogList
        restWaterUsageLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waterUsageLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].usageDate").value(hasItem(DEFAULT_USAGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amountUsed").value(hasItem(DEFAULT_AMOUNT_USED.doubleValue())));
    }

    @Test
    @Transactional
    void getWaterUsageLog() throws Exception {
        // Initialize the database
        insertedWaterUsageLog = waterUsageLogRepository.saveAndFlush(waterUsageLog);

        // Get the waterUsageLog
        restWaterUsageLogMockMvc
            .perform(get(ENTITY_API_URL_ID, waterUsageLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waterUsageLog.getId().intValue()))
            .andExpect(jsonPath("$.usageDate").value(DEFAULT_USAGE_DATE.toString()))
            .andExpect(jsonPath("$.amountUsed").value(DEFAULT_AMOUNT_USED.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingWaterUsageLog() throws Exception {
        // Get the waterUsageLog
        restWaterUsageLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWaterUsageLog() throws Exception {
        // Initialize the database
        insertedWaterUsageLog = waterUsageLogRepository.saveAndFlush(waterUsageLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterUsageLog
        WaterUsageLog updatedWaterUsageLog = waterUsageLogRepository.findById(waterUsageLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWaterUsageLog are not directly saved in db
        em.detach(updatedWaterUsageLog);
        updatedWaterUsageLog.usageDate(UPDATED_USAGE_DATE).amountUsed(UPDATED_AMOUNT_USED);
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(updatedWaterUsageLog);

        restWaterUsageLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waterUsageLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterUsageLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the WaterUsageLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWaterUsageLogToMatchAllProperties(updatedWaterUsageLog);
    }

    @Test
    @Transactional
    void putNonExistingWaterUsageLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterUsageLog.setId(longCount.incrementAndGet());

        // Create the WaterUsageLog
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaterUsageLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waterUsageLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterUsageLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterUsageLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWaterUsageLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterUsageLog.setId(longCount.incrementAndGet());

        // Create the WaterUsageLog
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterUsageLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterUsageLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterUsageLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWaterUsageLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterUsageLog.setId(longCount.incrementAndGet());

        // Create the WaterUsageLog
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterUsageLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterUsageLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaterUsageLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWaterUsageLogWithPatch() throws Exception {
        // Initialize the database
        insertedWaterUsageLog = waterUsageLogRepository.saveAndFlush(waterUsageLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterUsageLog using partial update
        WaterUsageLog partialUpdatedWaterUsageLog = new WaterUsageLog();
        partialUpdatedWaterUsageLog.setId(waterUsageLog.getId());

        partialUpdatedWaterUsageLog.amountUsed(UPDATED_AMOUNT_USED);

        restWaterUsageLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaterUsageLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWaterUsageLog))
            )
            .andExpect(status().isOk());

        // Validate the WaterUsageLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWaterUsageLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWaterUsageLog, waterUsageLog),
            getPersistedWaterUsageLog(waterUsageLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateWaterUsageLogWithPatch() throws Exception {
        // Initialize the database
        insertedWaterUsageLog = waterUsageLogRepository.saveAndFlush(waterUsageLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterUsageLog using partial update
        WaterUsageLog partialUpdatedWaterUsageLog = new WaterUsageLog();
        partialUpdatedWaterUsageLog.setId(waterUsageLog.getId());

        partialUpdatedWaterUsageLog.usageDate(UPDATED_USAGE_DATE).amountUsed(UPDATED_AMOUNT_USED);

        restWaterUsageLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaterUsageLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWaterUsageLog))
            )
            .andExpect(status().isOk());

        // Validate the WaterUsageLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWaterUsageLogUpdatableFieldsEquals(partialUpdatedWaterUsageLog, getPersistedWaterUsageLog(partialUpdatedWaterUsageLog));
    }

    @Test
    @Transactional
    void patchNonExistingWaterUsageLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterUsageLog.setId(longCount.incrementAndGet());

        // Create the WaterUsageLog
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaterUsageLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, waterUsageLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(waterUsageLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterUsageLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWaterUsageLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterUsageLog.setId(longCount.incrementAndGet());

        // Create the WaterUsageLog
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterUsageLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(waterUsageLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterUsageLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWaterUsageLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterUsageLog.setId(longCount.incrementAndGet());

        // Create the WaterUsageLog
        WaterUsageLogDTO waterUsageLogDTO = waterUsageLogMapper.toDto(waterUsageLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterUsageLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(waterUsageLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaterUsageLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWaterUsageLog() throws Exception {
        // Initialize the database
        insertedWaterUsageLog = waterUsageLogRepository.saveAndFlush(waterUsageLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the waterUsageLog
        restWaterUsageLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, waterUsageLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return waterUsageLogRepository.count();
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

    protected WaterUsageLog getPersistedWaterUsageLog(WaterUsageLog waterUsageLog) {
        return waterUsageLogRepository.findById(waterUsageLog.getId()).orElseThrow();
    }

    protected void assertPersistedWaterUsageLogToMatchAllProperties(WaterUsageLog expectedWaterUsageLog) {
        assertWaterUsageLogAllPropertiesEquals(expectedWaterUsageLog, getPersistedWaterUsageLog(expectedWaterUsageLog));
    }

    protected void assertPersistedWaterUsageLogToMatchUpdatableProperties(WaterUsageLog expectedWaterUsageLog) {
        assertWaterUsageLogAllUpdatablePropertiesEquals(expectedWaterUsageLog, getPersistedWaterUsageLog(expectedWaterUsageLog));
    }
}
