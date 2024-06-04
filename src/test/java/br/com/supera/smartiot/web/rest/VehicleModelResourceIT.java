package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.VehicleModelAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.VehicleModel;
import br.com.supera.smartiot.repository.VehicleModelRepository;
import br.com.supera.smartiot.service.dto.VehicleModelDTO;
import br.com.supera.smartiot.service.mapper.VehicleModelMapper;
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
 * Integration tests for the {@link VehicleModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleModelResourceIT {

    private static final String DEFAULT_MODEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Autowired
    private VehicleModelMapper vehicleModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleModelMockMvc;

    private VehicleModel vehicleModel;

    private VehicleModel insertedVehicleModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleModel createEntity(EntityManager em) {
        VehicleModel vehicleModel = new VehicleModel().modelName(DEFAULT_MODEL_NAME);
        return vehicleModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleModel createUpdatedEntity(EntityManager em) {
        VehicleModel vehicleModel = new VehicleModel().modelName(UPDATED_MODEL_NAME);
        return vehicleModel;
    }

    @BeforeEach
    public void initTest() {
        vehicleModel = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleModel != null) {
            vehicleModelRepository.delete(insertedVehicleModel);
            insertedVehicleModel = null;
        }
    }

    @Test
    @Transactional
    void createVehicleModel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleModel
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);
        var returnedVehicleModelDTO = om.readValue(
            restVehicleModelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleModelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleModelDTO.class
        );

        // Validate the VehicleModel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleModel = vehicleModelMapper.toEntity(returnedVehicleModelDTO);
        assertVehicleModelUpdatableFieldsEquals(returnedVehicleModel, getPersistedVehicleModel(returnedVehicleModel));

        insertedVehicleModel = returnedVehicleModel;
    }

    @Test
    @Transactional
    void createVehicleModelWithExistingId() throws Exception {
        // Create the VehicleModel with an existing ID
        vehicleModel.setId(1L);
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleModel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkModelNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleModel.setModelName(null);

        // Create the VehicleModel, which fails.
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);

        restVehicleModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleModelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleModels() throws Exception {
        // Initialize the database
        insertedVehicleModel = vehicleModelRepository.saveAndFlush(vehicleModel);

        // Get all the vehicleModelList
        restVehicleModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)));
    }

    @Test
    @Transactional
    void getVehicleModel() throws Exception {
        // Initialize the database
        insertedVehicleModel = vehicleModelRepository.saveAndFlush(vehicleModel);

        // Get the vehicleModel
        restVehicleModelMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleModel.getId().intValue()))
            .andExpect(jsonPath("$.modelName").value(DEFAULT_MODEL_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVehicleModel() throws Exception {
        // Get the vehicleModel
        restVehicleModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleModel() throws Exception {
        // Initialize the database
        insertedVehicleModel = vehicleModelRepository.saveAndFlush(vehicleModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleModel
        VehicleModel updatedVehicleModel = vehicleModelRepository.findById(vehicleModel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleModel are not directly saved in db
        em.detach(updatedVehicleModel);
        updatedVehicleModel.modelName(UPDATED_MODEL_NAME);
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(updatedVehicleModel);

        restVehicleModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleModelToMatchAllProperties(updatedVehicleModel);
    }

    @Test
    @Transactional
    void putNonExistingVehicleModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleModel.setId(longCount.incrementAndGet());

        // Create the VehicleModel
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleModel.setId(longCount.incrementAndGet());

        // Create the VehicleModel
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleModel.setId(longCount.incrementAndGet());

        // Create the VehicleModel
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleModelWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleModel = vehicleModelRepository.saveAndFlush(vehicleModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleModel using partial update
        VehicleModel partialUpdatedVehicleModel = new VehicleModel();
        partialUpdatedVehicleModel.setId(vehicleModel.getId());

        restVehicleModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleModel))
            )
            .andExpect(status().isOk());

        // Validate the VehicleModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleModelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleModel, vehicleModel),
            getPersistedVehicleModel(vehicleModel)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleModelWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleModel = vehicleModelRepository.saveAndFlush(vehicleModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleModel using partial update
        VehicleModel partialUpdatedVehicleModel = new VehicleModel();
        partialUpdatedVehicleModel.setId(vehicleModel.getId());

        partialUpdatedVehicleModel.modelName(UPDATED_MODEL_NAME);

        restVehicleModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleModel))
            )
            .andExpect(status().isOk());

        // Validate the VehicleModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleModelUpdatableFieldsEquals(partialUpdatedVehicleModel, getPersistedVehicleModel(partialUpdatedVehicleModel));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleModel.setId(longCount.incrementAndGet());

        // Create the VehicleModel
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleModel.setId(longCount.incrementAndGet());

        // Create the VehicleModel
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleModel.setId(longCount.incrementAndGet());

        // Create the VehicleModel
        VehicleModelDTO vehicleModelDTO = vehicleModelMapper.toDto(vehicleModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleModelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleModel() throws Exception {
        // Initialize the database
        insertedVehicleModel = vehicleModelRepository.saveAndFlush(vehicleModel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleModel
        restVehicleModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleModelRepository.count();
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

    protected VehicleModel getPersistedVehicleModel(VehicleModel vehicleModel) {
        return vehicleModelRepository.findById(vehicleModel.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleModelToMatchAllProperties(VehicleModel expectedVehicleModel) {
        assertVehicleModelAllPropertiesEquals(expectedVehicleModel, getPersistedVehicleModel(expectedVehicleModel));
    }

    protected void assertPersistedVehicleModelToMatchUpdatableProperties(VehicleModel expectedVehicleModel) {
        assertVehicleModelAllUpdatablePropertiesEquals(expectedVehicleModel, getPersistedVehicleModel(expectedVehicleModel));
    }
}
