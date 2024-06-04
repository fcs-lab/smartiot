package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.VehicleStatusLogAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.VehicleStatusLog;
import br.com.supera.smartiot.domain.enumeration.VehicleStatus;
import br.com.supera.smartiot.repository.VehicleStatusLogRepository;
import br.com.supera.smartiot.service.dto.VehicleStatusLogDTO;
import br.com.supera.smartiot.service.mapper.VehicleStatusLogMapper;
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
 * Integration tests for the {@link VehicleStatusLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleStatusLogResourceIT {

    private static final Instant DEFAULT_STATUS_CHANGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STATUS_CHANGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final VehicleStatus DEFAULT_NEW_STATUS = VehicleStatus.AVAILABLE;
    private static final VehicleStatus UPDATED_NEW_STATUS = VehicleStatus.MAINTENANCE;

    private static final String ENTITY_API_URL = "/api/vehicle-status-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleStatusLogRepository vehicleStatusLogRepository;

    @Autowired
    private VehicleStatusLogMapper vehicleStatusLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleStatusLogMockMvc;

    private VehicleStatusLog vehicleStatusLog;

    private VehicleStatusLog insertedVehicleStatusLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleStatusLog createEntity(EntityManager em) {
        VehicleStatusLog vehicleStatusLog = new VehicleStatusLog()
            .statusChangeDate(DEFAULT_STATUS_CHANGE_DATE)
            .newStatus(DEFAULT_NEW_STATUS);
        return vehicleStatusLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleStatusLog createUpdatedEntity(EntityManager em) {
        VehicleStatusLog vehicleStatusLog = new VehicleStatusLog()
            .statusChangeDate(UPDATED_STATUS_CHANGE_DATE)
            .newStatus(UPDATED_NEW_STATUS);
        return vehicleStatusLog;
    }

    @BeforeEach
    public void initTest() {
        vehicleStatusLog = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleStatusLog != null) {
            vehicleStatusLogRepository.delete(insertedVehicleStatusLog);
            insertedVehicleStatusLog = null;
        }
    }

    @Test
    @Transactional
    void createVehicleStatusLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleStatusLog
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);
        var returnedVehicleStatusLogDTO = om.readValue(
            restVehicleStatusLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleStatusLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleStatusLogDTO.class
        );

        // Validate the VehicleStatusLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleStatusLog = vehicleStatusLogMapper.toEntity(returnedVehicleStatusLogDTO);
        assertVehicleStatusLogUpdatableFieldsEquals(returnedVehicleStatusLog, getPersistedVehicleStatusLog(returnedVehicleStatusLog));

        insertedVehicleStatusLog = returnedVehicleStatusLog;
    }

    @Test
    @Transactional
    void createVehicleStatusLogWithExistingId() throws Exception {
        // Create the VehicleStatusLog with an existing ID
        vehicleStatusLog.setId(1L);
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleStatusLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleStatusLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleStatusLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusChangeDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleStatusLog.setStatusChangeDate(null);

        // Create the VehicleStatusLog, which fails.
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        restVehicleStatusLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleStatusLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleStatusLog.setNewStatus(null);

        // Create the VehicleStatusLog, which fails.
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        restVehicleStatusLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleStatusLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleStatusLogs() throws Exception {
        // Initialize the database
        insertedVehicleStatusLog = vehicleStatusLogRepository.saveAndFlush(vehicleStatusLog);

        // Get all the vehicleStatusLogList
        restVehicleStatusLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleStatusLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusChangeDate").value(hasItem(DEFAULT_STATUS_CHANGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].newStatus").value(hasItem(DEFAULT_NEW_STATUS.toString())));
    }

    @Test
    @Transactional
    void getVehicleStatusLog() throws Exception {
        // Initialize the database
        insertedVehicleStatusLog = vehicleStatusLogRepository.saveAndFlush(vehicleStatusLog);

        // Get the vehicleStatusLog
        restVehicleStatusLogMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleStatusLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleStatusLog.getId().intValue()))
            .andExpect(jsonPath("$.statusChangeDate").value(DEFAULT_STATUS_CHANGE_DATE.toString()))
            .andExpect(jsonPath("$.newStatus").value(DEFAULT_NEW_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVehicleStatusLog() throws Exception {
        // Get the vehicleStatusLog
        restVehicleStatusLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleStatusLog() throws Exception {
        // Initialize the database
        insertedVehicleStatusLog = vehicleStatusLogRepository.saveAndFlush(vehicleStatusLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleStatusLog
        VehicleStatusLog updatedVehicleStatusLog = vehicleStatusLogRepository.findById(vehicleStatusLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleStatusLog are not directly saved in db
        em.detach(updatedVehicleStatusLog);
        updatedVehicleStatusLog.statusChangeDate(UPDATED_STATUS_CHANGE_DATE).newStatus(UPDATED_NEW_STATUS);
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(updatedVehicleStatusLog);

        restVehicleStatusLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleStatusLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleStatusLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleStatusLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleStatusLogToMatchAllProperties(updatedVehicleStatusLog);
    }

    @Test
    @Transactional
    void putNonExistingVehicleStatusLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleStatusLog.setId(longCount.incrementAndGet());

        // Create the VehicleStatusLog
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleStatusLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleStatusLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleStatusLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleStatusLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleStatusLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleStatusLog.setId(longCount.incrementAndGet());

        // Create the VehicleStatusLog
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleStatusLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleStatusLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleStatusLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleStatusLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleStatusLog.setId(longCount.incrementAndGet());

        // Create the VehicleStatusLog
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleStatusLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleStatusLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleStatusLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleStatusLogWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleStatusLog = vehicleStatusLogRepository.saveAndFlush(vehicleStatusLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleStatusLog using partial update
        VehicleStatusLog partialUpdatedVehicleStatusLog = new VehicleStatusLog();
        partialUpdatedVehicleStatusLog.setId(vehicleStatusLog.getId());

        partialUpdatedVehicleStatusLog.statusChangeDate(UPDATED_STATUS_CHANGE_DATE);

        restVehicleStatusLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleStatusLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleStatusLog))
            )
            .andExpect(status().isOk());

        // Validate the VehicleStatusLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleStatusLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleStatusLog, vehicleStatusLog),
            getPersistedVehicleStatusLog(vehicleStatusLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleStatusLogWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleStatusLog = vehicleStatusLogRepository.saveAndFlush(vehicleStatusLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleStatusLog using partial update
        VehicleStatusLog partialUpdatedVehicleStatusLog = new VehicleStatusLog();
        partialUpdatedVehicleStatusLog.setId(vehicleStatusLog.getId());

        partialUpdatedVehicleStatusLog.statusChangeDate(UPDATED_STATUS_CHANGE_DATE).newStatus(UPDATED_NEW_STATUS);

        restVehicleStatusLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleStatusLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleStatusLog))
            )
            .andExpect(status().isOk());

        // Validate the VehicleStatusLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleStatusLogUpdatableFieldsEquals(
            partialUpdatedVehicleStatusLog,
            getPersistedVehicleStatusLog(partialUpdatedVehicleStatusLog)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVehicleStatusLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleStatusLog.setId(longCount.incrementAndGet());

        // Create the VehicleStatusLog
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleStatusLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleStatusLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleStatusLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleStatusLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleStatusLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleStatusLog.setId(longCount.incrementAndGet());

        // Create the VehicleStatusLog
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleStatusLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleStatusLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleStatusLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleStatusLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleStatusLog.setId(longCount.incrementAndGet());

        // Create the VehicleStatusLog
        VehicleStatusLogDTO vehicleStatusLogDTO = vehicleStatusLogMapper.toDto(vehicleStatusLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleStatusLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleStatusLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleStatusLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleStatusLog() throws Exception {
        // Initialize the database
        insertedVehicleStatusLog = vehicleStatusLogRepository.saveAndFlush(vehicleStatusLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleStatusLog
        restVehicleStatusLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleStatusLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleStatusLogRepository.count();
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

    protected VehicleStatusLog getPersistedVehicleStatusLog(VehicleStatusLog vehicleStatusLog) {
        return vehicleStatusLogRepository.findById(vehicleStatusLog.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleStatusLogToMatchAllProperties(VehicleStatusLog expectedVehicleStatusLog) {
        assertVehicleStatusLogAllPropertiesEquals(expectedVehicleStatusLog, getPersistedVehicleStatusLog(expectedVehicleStatusLog));
    }

    protected void assertPersistedVehicleStatusLogToMatchUpdatableProperties(VehicleStatusLog expectedVehicleStatusLog) {
        assertVehicleStatusLogAllUpdatablePropertiesEquals(
            expectedVehicleStatusLog,
            getPersistedVehicleStatusLog(expectedVehicleStatusLog)
        );
    }
}
