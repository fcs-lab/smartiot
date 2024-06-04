package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.VehicleSubStatusAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.VehicleSubStatus;
import br.com.supera.smartiot.repository.VehicleSubStatusRepository;
import br.com.supera.smartiot.service.dto.VehicleSubStatusDTO;
import br.com.supera.smartiot.service.mapper.VehicleSubStatusMapper;
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
 * Integration tests for the {@link VehicleSubStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleSubStatusResourceIT {

    private static final String DEFAULT_SUB_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_STATUS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-sub-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleSubStatusRepository vehicleSubStatusRepository;

    @Autowired
    private VehicleSubStatusMapper vehicleSubStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleSubStatusMockMvc;

    private VehicleSubStatus vehicleSubStatus;

    private VehicleSubStatus insertedVehicleSubStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleSubStatus createEntity(EntityManager em) {
        VehicleSubStatus vehicleSubStatus = new VehicleSubStatus().subStatusName(DEFAULT_SUB_STATUS_NAME);
        return vehicleSubStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleSubStatus createUpdatedEntity(EntityManager em) {
        VehicleSubStatus vehicleSubStatus = new VehicleSubStatus().subStatusName(UPDATED_SUB_STATUS_NAME);
        return vehicleSubStatus;
    }

    @BeforeEach
    public void initTest() {
        vehicleSubStatus = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleSubStatus != null) {
            vehicleSubStatusRepository.delete(insertedVehicleSubStatus);
            insertedVehicleSubStatus = null;
        }
    }

    @Test
    @Transactional
    void createVehicleSubStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleSubStatus
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);
        var returnedVehicleSubStatusDTO = om.readValue(
            restVehicleSubStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleSubStatusDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleSubStatusDTO.class
        );

        // Validate the VehicleSubStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleSubStatus = vehicleSubStatusMapper.toEntity(returnedVehicleSubStatusDTO);
        assertVehicleSubStatusUpdatableFieldsEquals(returnedVehicleSubStatus, getPersistedVehicleSubStatus(returnedVehicleSubStatus));

        insertedVehicleSubStatus = returnedVehicleSubStatus;
    }

    @Test
    @Transactional
    void createVehicleSubStatusWithExistingId() throws Exception {
        // Create the VehicleSubStatus with an existing ID
        vehicleSubStatus.setId(1L);
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleSubStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleSubStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleSubStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubStatusNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleSubStatus.setSubStatusName(null);

        // Create the VehicleSubStatus, which fails.
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);

        restVehicleSubStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleSubStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleSubStatuses() throws Exception {
        // Initialize the database
        insertedVehicleSubStatus = vehicleSubStatusRepository.saveAndFlush(vehicleSubStatus);

        // Get all the vehicleSubStatusList
        restVehicleSubStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleSubStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].subStatusName").value(hasItem(DEFAULT_SUB_STATUS_NAME)));
    }

    @Test
    @Transactional
    void getVehicleSubStatus() throws Exception {
        // Initialize the database
        insertedVehicleSubStatus = vehicleSubStatusRepository.saveAndFlush(vehicleSubStatus);

        // Get the vehicleSubStatus
        restVehicleSubStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleSubStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleSubStatus.getId().intValue()))
            .andExpect(jsonPath("$.subStatusName").value(DEFAULT_SUB_STATUS_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVehicleSubStatus() throws Exception {
        // Get the vehicleSubStatus
        restVehicleSubStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleSubStatus() throws Exception {
        // Initialize the database
        insertedVehicleSubStatus = vehicleSubStatusRepository.saveAndFlush(vehicleSubStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleSubStatus
        VehicleSubStatus updatedVehicleSubStatus = vehicleSubStatusRepository.findById(vehicleSubStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleSubStatus are not directly saved in db
        em.detach(updatedVehicleSubStatus);
        updatedVehicleSubStatus.subStatusName(UPDATED_SUB_STATUS_NAME);
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(updatedVehicleSubStatus);

        restVehicleSubStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleSubStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleSubStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleSubStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleSubStatusToMatchAllProperties(updatedVehicleSubStatus);
    }

    @Test
    @Transactional
    void putNonExistingVehicleSubStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleSubStatus.setId(longCount.incrementAndGet());

        // Create the VehicleSubStatus
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleSubStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleSubStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleSubStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleSubStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleSubStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleSubStatus.setId(longCount.incrementAndGet());

        // Create the VehicleSubStatus
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleSubStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleSubStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleSubStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleSubStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleSubStatus.setId(longCount.incrementAndGet());

        // Create the VehicleSubStatus
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleSubStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleSubStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleSubStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleSubStatusWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleSubStatus = vehicleSubStatusRepository.saveAndFlush(vehicleSubStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleSubStatus using partial update
        VehicleSubStatus partialUpdatedVehicleSubStatus = new VehicleSubStatus();
        partialUpdatedVehicleSubStatus.setId(vehicleSubStatus.getId());

        restVehicleSubStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleSubStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleSubStatus))
            )
            .andExpect(status().isOk());

        // Validate the VehicleSubStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleSubStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleSubStatus, vehicleSubStatus),
            getPersistedVehicleSubStatus(vehicleSubStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleSubStatusWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleSubStatus = vehicleSubStatusRepository.saveAndFlush(vehicleSubStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleSubStatus using partial update
        VehicleSubStatus partialUpdatedVehicleSubStatus = new VehicleSubStatus();
        partialUpdatedVehicleSubStatus.setId(vehicleSubStatus.getId());

        partialUpdatedVehicleSubStatus.subStatusName(UPDATED_SUB_STATUS_NAME);

        restVehicleSubStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleSubStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleSubStatus))
            )
            .andExpect(status().isOk());

        // Validate the VehicleSubStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleSubStatusUpdatableFieldsEquals(
            partialUpdatedVehicleSubStatus,
            getPersistedVehicleSubStatus(partialUpdatedVehicleSubStatus)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVehicleSubStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleSubStatus.setId(longCount.incrementAndGet());

        // Create the VehicleSubStatus
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleSubStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleSubStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleSubStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleSubStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleSubStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleSubStatus.setId(longCount.incrementAndGet());

        // Create the VehicleSubStatus
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleSubStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleSubStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleSubStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleSubStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleSubStatus.setId(longCount.incrementAndGet());

        // Create the VehicleSubStatus
        VehicleSubStatusDTO vehicleSubStatusDTO = vehicleSubStatusMapper.toDto(vehicleSubStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleSubStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleSubStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleSubStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleSubStatus() throws Exception {
        // Initialize the database
        insertedVehicleSubStatus = vehicleSubStatusRepository.saveAndFlush(vehicleSubStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleSubStatus
        restVehicleSubStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleSubStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleSubStatusRepository.count();
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

    protected VehicleSubStatus getPersistedVehicleSubStatus(VehicleSubStatus vehicleSubStatus) {
        return vehicleSubStatusRepository.findById(vehicleSubStatus.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleSubStatusToMatchAllProperties(VehicleSubStatus expectedVehicleSubStatus) {
        assertVehicleSubStatusAllPropertiesEquals(expectedVehicleSubStatus, getPersistedVehicleSubStatus(expectedVehicleSubStatus));
    }

    protected void assertPersistedVehicleSubStatusToMatchUpdatableProperties(VehicleSubStatus expectedVehicleSubStatus) {
        assertVehicleSubStatusAllUpdatablePropertiesEquals(
            expectedVehicleSubStatus,
            getPersistedVehicleSubStatus(expectedVehicleSubStatus)
        );
    }
}
