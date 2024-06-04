package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.VehicleServiceAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.VehicleService;
import br.com.supera.smartiot.repository.VehicleServiceRepository;
import br.com.supera.smartiot.service.dto.VehicleServiceDTO;
import br.com.supera.smartiot.service.mapper.VehicleServiceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link VehicleServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleServiceResourceIT {

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SERVICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SERVICE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SERVICE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleServiceRepository vehicleServiceRepository;

    @Autowired
    private VehicleServiceMapper vehicleServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleServiceMockMvc;

    private VehicleService vehicleService;

    private VehicleService insertedVehicleService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleService createEntity(EntityManager em) {
        VehicleService vehicleService = new VehicleService()
            .serviceName(DEFAULT_SERVICE_NAME)
            .serviceDate(DEFAULT_SERVICE_DATE)
            .serviceDescription(DEFAULT_SERVICE_DESCRIPTION);
        return vehicleService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleService createUpdatedEntity(EntityManager em) {
        VehicleService vehicleService = new VehicleService()
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceDate(UPDATED_SERVICE_DATE)
            .serviceDescription(UPDATED_SERVICE_DESCRIPTION);
        return vehicleService;
    }

    @BeforeEach
    public void initTest() {
        vehicleService = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleService != null) {
            vehicleServiceRepository.delete(insertedVehicleService);
            insertedVehicleService = null;
        }
    }

    @Test
    @Transactional
    void createVehicleService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleService
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);
        var returnedVehicleServiceDTO = om.readValue(
            restVehicleServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleServiceDTO.class
        );

        // Validate the VehicleService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleService = vehicleServiceMapper.toEntity(returnedVehicleServiceDTO);
        assertVehicleServiceUpdatableFieldsEquals(returnedVehicleService, getPersistedVehicleService(returnedVehicleService));

        insertedVehicleService = returnedVehicleService;
    }

    @Test
    @Transactional
    void createVehicleServiceWithExistingId() throws Exception {
        // Create the VehicleService with an existing ID
        vehicleService.setId(1L);
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkServiceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleService.setServiceName(null);

        // Create the VehicleService, which fails.
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        restVehicleServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleService.setServiceDate(null);

        // Create the VehicleService, which fails.
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        restVehicleServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleServices() throws Exception {
        // Initialize the database
        insertedVehicleService = vehicleServiceRepository.saveAndFlush(vehicleService);

        // Get all the vehicleServiceList
        restVehicleServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleService.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].serviceDate").value(hasItem(DEFAULT_SERVICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].serviceDescription").value(hasItem(DEFAULT_SERVICE_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getVehicleService() throws Exception {
        // Initialize the database
        insertedVehicleService = vehicleServiceRepository.saveAndFlush(vehicleService);

        // Get the vehicleService
        restVehicleServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleService.getId().intValue()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.serviceDate").value(DEFAULT_SERVICE_DATE.toString()))
            .andExpect(jsonPath("$.serviceDescription").value(DEFAULT_SERVICE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingVehicleService() throws Exception {
        // Get the vehicleService
        restVehicleServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleService() throws Exception {
        // Initialize the database
        insertedVehicleService = vehicleServiceRepository.saveAndFlush(vehicleService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleService
        VehicleService updatedVehicleService = vehicleServiceRepository.findById(vehicleService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleService are not directly saved in db
        em.detach(updatedVehicleService);
        updatedVehicleService
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceDate(UPDATED_SERVICE_DATE)
            .serviceDescription(UPDATED_SERVICE_DESCRIPTION);
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(updatedVehicleService);

        restVehicleServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleServiceToMatchAllProperties(updatedVehicleService);
    }

    @Test
    @Transactional
    void putNonExistingVehicleService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleService.setId(longCount.incrementAndGet());

        // Create the VehicleService
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleService.setId(longCount.incrementAndGet());

        // Create the VehicleService
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleService.setId(longCount.incrementAndGet());

        // Create the VehicleService
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleServiceWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleService = vehicleServiceRepository.saveAndFlush(vehicleService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleService using partial update
        VehicleService partialUpdatedVehicleService = new VehicleService();
        partialUpdatedVehicleService.setId(vehicleService.getId());

        partialUpdatedVehicleService.serviceDescription(UPDATED_SERVICE_DESCRIPTION);

        restVehicleServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleService))
            )
            .andExpect(status().isOk());

        // Validate the VehicleService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleService, vehicleService),
            getPersistedVehicleService(vehicleService)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleServiceWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleService = vehicleServiceRepository.saveAndFlush(vehicleService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleService using partial update
        VehicleService partialUpdatedVehicleService = new VehicleService();
        partialUpdatedVehicleService.setId(vehicleService.getId());

        partialUpdatedVehicleService
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceDate(UPDATED_SERVICE_DATE)
            .serviceDescription(UPDATED_SERVICE_DESCRIPTION);

        restVehicleServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleService))
            )
            .andExpect(status().isOk());

        // Validate the VehicleService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleServiceUpdatableFieldsEquals(partialUpdatedVehicleService, getPersistedVehicleService(partialUpdatedVehicleService));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleService.setId(longCount.incrementAndGet());

        // Create the VehicleService
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleService.setId(longCount.incrementAndGet());

        // Create the VehicleService
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleService.setId(longCount.incrementAndGet());

        // Create the VehicleService
        VehicleServiceDTO vehicleServiceDTO = vehicleServiceMapper.toDto(vehicleService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleService() throws Exception {
        // Initialize the database
        insertedVehicleService = vehicleServiceRepository.saveAndFlush(vehicleService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleService
        restVehicleServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleServiceRepository.count();
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

    protected VehicleService getPersistedVehicleService(VehicleService vehicleService) {
        return vehicleServiceRepository.findById(vehicleService.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleServiceToMatchAllProperties(VehicleService expectedVehicleService) {
        assertVehicleServiceAllPropertiesEquals(expectedVehicleService, getPersistedVehicleService(expectedVehicleService));
    }

    protected void assertPersistedVehicleServiceToMatchUpdatableProperties(VehicleService expectedVehicleService) {
        assertVehicleServiceAllUpdatablePropertiesEquals(expectedVehicleService, getPersistedVehicleService(expectedVehicleService));
    }
}
