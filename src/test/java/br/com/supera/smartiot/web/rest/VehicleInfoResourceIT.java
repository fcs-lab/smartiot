package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.VehicleInfoAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.VehicleInfo;
import br.com.supera.smartiot.domain.enumeration.VehicleStatus;
import br.com.supera.smartiot.repository.VehicleInfoRepository;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
import br.com.supera.smartiot.service.mapper.VehicleInfoMapper;
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
 * Integration tests for the {@link VehicleInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleInfoResourceIT {

    private static final String DEFAULT_MODEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_PLATE = "BBBBBBBBBB";

    private static final VehicleStatus DEFAULT_VEHICLE_STATUS = VehicleStatus.AVAILABLE;
    private static final VehicleStatus UPDATED_VEHICLE_STATUS = VehicleStatus.MAINTENANCE;

    private static final String ENTITY_API_URL = "/api/vehicle-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleInfoRepository vehicleInfoRepository;

    @Autowired
    private VehicleInfoMapper vehicleInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleInfoMockMvc;

    private VehicleInfo vehicleInfo;

    private VehicleInfo insertedVehicleInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleInfo createEntity(EntityManager em) {
        VehicleInfo vehicleInfo = new VehicleInfo()
            .modelName(DEFAULT_MODEL_NAME)
            .licensePlate(DEFAULT_LICENSE_PLATE)
            .vehicleStatus(DEFAULT_VEHICLE_STATUS);
        return vehicleInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleInfo createUpdatedEntity(EntityManager em) {
        VehicleInfo vehicleInfo = new VehicleInfo()
            .modelName(UPDATED_MODEL_NAME)
            .licensePlate(UPDATED_LICENSE_PLATE)
            .vehicleStatus(UPDATED_VEHICLE_STATUS);
        return vehicleInfo;
    }

    @BeforeEach
    public void initTest() {
        vehicleInfo = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleInfo != null) {
            vehicleInfoRepository.delete(insertedVehicleInfo);
            insertedVehicleInfo = null;
        }
    }

    @Test
    @Transactional
    void createVehicleInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleInfo
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);
        var returnedVehicleInfoDTO = om.readValue(
            restVehicleInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleInfoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleInfoDTO.class
        );

        // Validate the VehicleInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleInfo = vehicleInfoMapper.toEntity(returnedVehicleInfoDTO);
        assertVehicleInfoUpdatableFieldsEquals(returnedVehicleInfo, getPersistedVehicleInfo(returnedVehicleInfo));

        insertedVehicleInfo = returnedVehicleInfo;
    }

    @Test
    @Transactional
    void createVehicleInfoWithExistingId() throws Exception {
        // Create the VehicleInfo with an existing ID
        vehicleInfo.setId(1L);
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkModelNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleInfo.setModelName(null);

        // Create the VehicleInfo, which fails.
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        restVehicleInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicensePlateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleInfo.setLicensePlate(null);

        // Create the VehicleInfo, which fails.
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        restVehicleInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVehicleStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleInfo.setVehicleStatus(null);

        // Create the VehicleInfo, which fails.
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        restVehicleInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleInfos() throws Exception {
        // Initialize the database
        insertedVehicleInfo = vehicleInfoRepository.saveAndFlush(vehicleInfo);

        // Get all the vehicleInfoList
        restVehicleInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)))
            .andExpect(jsonPath("$.[*].licensePlate").value(hasItem(DEFAULT_LICENSE_PLATE)))
            .andExpect(jsonPath("$.[*].vehicleStatus").value(hasItem(DEFAULT_VEHICLE_STATUS.toString())));
    }

    @Test
    @Transactional
    void getVehicleInfo() throws Exception {
        // Initialize the database
        insertedVehicleInfo = vehicleInfoRepository.saveAndFlush(vehicleInfo);

        // Get the vehicleInfo
        restVehicleInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleInfo.getId().intValue()))
            .andExpect(jsonPath("$.modelName").value(DEFAULT_MODEL_NAME))
            .andExpect(jsonPath("$.licensePlate").value(DEFAULT_LICENSE_PLATE))
            .andExpect(jsonPath("$.vehicleStatus").value(DEFAULT_VEHICLE_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVehicleInfo() throws Exception {
        // Get the vehicleInfo
        restVehicleInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleInfo() throws Exception {
        // Initialize the database
        insertedVehicleInfo = vehicleInfoRepository.saveAndFlush(vehicleInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleInfo
        VehicleInfo updatedVehicleInfo = vehicleInfoRepository.findById(vehicleInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleInfo are not directly saved in db
        em.detach(updatedVehicleInfo);
        updatedVehicleInfo.modelName(UPDATED_MODEL_NAME).licensePlate(UPDATED_LICENSE_PLATE).vehicleStatus(UPDATED_VEHICLE_STATUS);
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(updatedVehicleInfo);

        restVehicleInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleInfoToMatchAllProperties(updatedVehicleInfo);
    }

    @Test
    @Transactional
    void putNonExistingVehicleInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleInfo.setId(longCount.incrementAndGet());

        // Create the VehicleInfo
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleInfo.setId(longCount.incrementAndGet());

        // Create the VehicleInfo
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleInfo.setId(longCount.incrementAndGet());

        // Create the VehicleInfo
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleInfoWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleInfo = vehicleInfoRepository.saveAndFlush(vehicleInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleInfo using partial update
        VehicleInfo partialUpdatedVehicleInfo = new VehicleInfo();
        partialUpdatedVehicleInfo.setId(vehicleInfo.getId());

        restVehicleInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleInfo))
            )
            .andExpect(status().isOk());

        // Validate the VehicleInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleInfo, vehicleInfo),
            getPersistedVehicleInfo(vehicleInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleInfoWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleInfo = vehicleInfoRepository.saveAndFlush(vehicleInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleInfo using partial update
        VehicleInfo partialUpdatedVehicleInfo = new VehicleInfo();
        partialUpdatedVehicleInfo.setId(vehicleInfo.getId());

        partialUpdatedVehicleInfo.modelName(UPDATED_MODEL_NAME).licensePlate(UPDATED_LICENSE_PLATE).vehicleStatus(UPDATED_VEHICLE_STATUS);

        restVehicleInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleInfo))
            )
            .andExpect(status().isOk());

        // Validate the VehicleInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleInfoUpdatableFieldsEquals(partialUpdatedVehicleInfo, getPersistedVehicleInfo(partialUpdatedVehicleInfo));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleInfo.setId(longCount.incrementAndGet());

        // Create the VehicleInfo
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleInfo.setId(longCount.incrementAndGet());

        // Create the VehicleInfo
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleInfo.setId(longCount.incrementAndGet());

        // Create the VehicleInfo
        VehicleInfoDTO vehicleInfoDTO = vehicleInfoMapper.toDto(vehicleInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleInfo() throws Exception {
        // Initialize the database
        insertedVehicleInfo = vehicleInfoRepository.saveAndFlush(vehicleInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleInfo
        restVehicleInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleInfoRepository.count();
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

    protected VehicleInfo getPersistedVehicleInfo(VehicleInfo vehicleInfo) {
        return vehicleInfoRepository.findById(vehicleInfo.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleInfoToMatchAllProperties(VehicleInfo expectedVehicleInfo) {
        assertVehicleInfoAllPropertiesEquals(expectedVehicleInfo, getPersistedVehicleInfo(expectedVehicleInfo));
    }

    protected void assertPersistedVehicleInfoToMatchUpdatableProperties(VehicleInfo expectedVehicleInfo) {
        assertVehicleInfoAllUpdatablePropertiesEquals(expectedVehicleInfo, getPersistedVehicleInfo(expectedVehicleInfo));
    }
}
