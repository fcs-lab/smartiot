package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.VehicleGroupAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.VehicleGroup;
import br.com.supera.smartiot.repository.VehicleGroupRepository;
import br.com.supera.smartiot.service.dto.VehicleGroupDTO;
import br.com.supera.smartiot.service.mapper.VehicleGroupMapper;
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
 * Integration tests for the {@link VehicleGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleGroupResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleGroupRepository vehicleGroupRepository;

    @Autowired
    private VehicleGroupMapper vehicleGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleGroupMockMvc;

    private VehicleGroup vehicleGroup;

    private VehicleGroup insertedVehicleGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleGroup createEntity(EntityManager em) {
        VehicleGroup vehicleGroup = new VehicleGroup().groupName(DEFAULT_GROUP_NAME).groupDescription(DEFAULT_GROUP_DESCRIPTION);
        return vehicleGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleGroup createUpdatedEntity(EntityManager em) {
        VehicleGroup vehicleGroup = new VehicleGroup().groupName(UPDATED_GROUP_NAME).groupDescription(UPDATED_GROUP_DESCRIPTION);
        return vehicleGroup;
    }

    @BeforeEach
    public void initTest() {
        vehicleGroup = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleGroup != null) {
            vehicleGroupRepository.delete(insertedVehicleGroup);
            insertedVehicleGroup = null;
        }
    }

    @Test
    @Transactional
    void createVehicleGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleGroup
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);
        var returnedVehicleGroupDTO = om.readValue(
            restVehicleGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleGroupDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleGroupDTO.class
        );

        // Validate the VehicleGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleGroup = vehicleGroupMapper.toEntity(returnedVehicleGroupDTO);
        assertVehicleGroupUpdatableFieldsEquals(returnedVehicleGroup, getPersistedVehicleGroup(returnedVehicleGroup));

        insertedVehicleGroup = returnedVehicleGroup;
    }

    @Test
    @Transactional
    void createVehicleGroupWithExistingId() throws Exception {
        // Create the VehicleGroup with an existing ID
        vehicleGroup.setId(1L);
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGroupNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleGroup.setGroupName(null);

        // Create the VehicleGroup, which fails.
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);

        restVehicleGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleGroups() throws Exception {
        // Initialize the database
        insertedVehicleGroup = vehicleGroupRepository.saveAndFlush(vehicleGroup);

        // Get all the vehicleGroupList
        restVehicleGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].groupDescription").value(hasItem(DEFAULT_GROUP_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getVehicleGroup() throws Exception {
        // Initialize the database
        insertedVehicleGroup = vehicleGroupRepository.saveAndFlush(vehicleGroup);

        // Get the vehicleGroup
        restVehicleGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.groupDescription").value(DEFAULT_GROUP_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingVehicleGroup() throws Exception {
        // Get the vehicleGroup
        restVehicleGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleGroup() throws Exception {
        // Initialize the database
        insertedVehicleGroup = vehicleGroupRepository.saveAndFlush(vehicleGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleGroup
        VehicleGroup updatedVehicleGroup = vehicleGroupRepository.findById(vehicleGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleGroup are not directly saved in db
        em.detach(updatedVehicleGroup);
        updatedVehicleGroup.groupName(UPDATED_GROUP_NAME).groupDescription(UPDATED_GROUP_DESCRIPTION);
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(updatedVehicleGroup);

        restVehicleGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleGroupToMatchAllProperties(updatedVehicleGroup);
    }

    @Test
    @Transactional
    void putNonExistingVehicleGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleGroup.setId(longCount.incrementAndGet());

        // Create the VehicleGroup
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleGroup.setId(longCount.incrementAndGet());

        // Create the VehicleGroup
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleGroup.setId(longCount.incrementAndGet());

        // Create the VehicleGroup
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleGroupWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleGroup = vehicleGroupRepository.saveAndFlush(vehicleGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleGroup using partial update
        VehicleGroup partialUpdatedVehicleGroup = new VehicleGroup();
        partialUpdatedVehicleGroup.setId(vehicleGroup.getId());

        partialUpdatedVehicleGroup.groupDescription(UPDATED_GROUP_DESCRIPTION);

        restVehicleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleGroup))
            )
            .andExpect(status().isOk());

        // Validate the VehicleGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleGroup, vehicleGroup),
            getPersistedVehicleGroup(vehicleGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleGroupWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleGroup = vehicleGroupRepository.saveAndFlush(vehicleGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleGroup using partial update
        VehicleGroup partialUpdatedVehicleGroup = new VehicleGroup();
        partialUpdatedVehicleGroup.setId(vehicleGroup.getId());

        partialUpdatedVehicleGroup.groupName(UPDATED_GROUP_NAME).groupDescription(UPDATED_GROUP_DESCRIPTION);

        restVehicleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleGroup))
            )
            .andExpect(status().isOk());

        // Validate the VehicleGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleGroupUpdatableFieldsEquals(partialUpdatedVehicleGroup, getPersistedVehicleGroup(partialUpdatedVehicleGroup));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleGroup.setId(longCount.incrementAndGet());

        // Create the VehicleGroup
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleGroup.setId(longCount.incrementAndGet());

        // Create the VehicleGroup
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleGroup.setId(longCount.incrementAndGet());

        // Create the VehicleGroup
        VehicleGroupDTO vehicleGroupDTO = vehicleGroupMapper.toDto(vehicleGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleGroup() throws Exception {
        // Initialize the database
        insertedVehicleGroup = vehicleGroupRepository.saveAndFlush(vehicleGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleGroup
        restVehicleGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleGroupRepository.count();
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

    protected VehicleGroup getPersistedVehicleGroup(VehicleGroup vehicleGroup) {
        return vehicleGroupRepository.findById(vehicleGroup.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleGroupToMatchAllProperties(VehicleGroup expectedVehicleGroup) {
        assertVehicleGroupAllPropertiesEquals(expectedVehicleGroup, getPersistedVehicleGroup(expectedVehicleGroup));
    }

    protected void assertPersistedVehicleGroupToMatchUpdatableProperties(VehicleGroup expectedVehicleGroup) {
        assertVehicleGroupAllUpdatablePropertiesEquals(expectedVehicleGroup, getPersistedVehicleGroup(expectedVehicleGroup));
    }
}
