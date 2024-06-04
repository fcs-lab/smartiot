package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.VehicleDamageAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.VehicleDamage;
import br.com.supera.smartiot.domain.enumeration.DamageStatus;
import br.com.supera.smartiot.repository.VehicleDamageRepository;
import br.com.supera.smartiot.service.dto.VehicleDamageDTO;
import br.com.supera.smartiot.service.mapper.VehicleDamageMapper;
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
 * Integration tests for the {@link VehicleDamageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleDamageResourceIT {

    private static final String DEFAULT_DAMAGE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_REPORTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REPORTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DamageStatus DEFAULT_DAMAGE_STATUS = DamageStatus.REPORTED;
    private static final DamageStatus UPDATED_DAMAGE_STATUS = DamageStatus.IN_PROGRESS;

    private static final String ENTITY_API_URL = "/api/vehicle-damages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleDamageRepository vehicleDamageRepository;

    @Autowired
    private VehicleDamageMapper vehicleDamageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleDamageMockMvc;

    private VehicleDamage vehicleDamage;

    private VehicleDamage insertedVehicleDamage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleDamage createEntity(EntityManager em) {
        VehicleDamage vehicleDamage = new VehicleDamage()
            .damageDescription(DEFAULT_DAMAGE_DESCRIPTION)
            .reportedAt(DEFAULT_REPORTED_AT)
            .damageStatus(DEFAULT_DAMAGE_STATUS);
        return vehicleDamage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleDamage createUpdatedEntity(EntityManager em) {
        VehicleDamage vehicleDamage = new VehicleDamage()
            .damageDescription(UPDATED_DAMAGE_DESCRIPTION)
            .reportedAt(UPDATED_REPORTED_AT)
            .damageStatus(UPDATED_DAMAGE_STATUS);
        return vehicleDamage;
    }

    @BeforeEach
    public void initTest() {
        vehicleDamage = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleDamage != null) {
            vehicleDamageRepository.delete(insertedVehicleDamage);
            insertedVehicleDamage = null;
        }
    }

    @Test
    @Transactional
    void createVehicleDamage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleDamage
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);
        var returnedVehicleDamageDTO = om.readValue(
            restVehicleDamageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDamageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleDamageDTO.class
        );

        // Validate the VehicleDamage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicleDamage = vehicleDamageMapper.toEntity(returnedVehicleDamageDTO);
        assertVehicleDamageUpdatableFieldsEquals(returnedVehicleDamage, getPersistedVehicleDamage(returnedVehicleDamage));

        insertedVehicleDamage = returnedVehicleDamage;
    }

    @Test
    @Transactional
    void createVehicleDamageWithExistingId() throws Exception {
        // Create the VehicleDamage with an existing ID
        vehicleDamage.setId(1L);
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleDamageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDamageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDamageDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleDamage.setDamageDescription(null);

        // Create the VehicleDamage, which fails.
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        restVehicleDamageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDamageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleDamage.setReportedAt(null);

        // Create the VehicleDamage, which fails.
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        restVehicleDamageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDamageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDamageStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleDamage.setDamageStatus(null);

        // Create the VehicleDamage, which fails.
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        restVehicleDamageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDamageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleDamages() throws Exception {
        // Initialize the database
        insertedVehicleDamage = vehicleDamageRepository.saveAndFlush(vehicleDamage);

        // Get all the vehicleDamageList
        restVehicleDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleDamage.getId().intValue())))
            .andExpect(jsonPath("$.[*].damageDescription").value(hasItem(DEFAULT_DAMAGE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].reportedAt").value(hasItem(DEFAULT_REPORTED_AT.toString())))
            .andExpect(jsonPath("$.[*].damageStatus").value(hasItem(DEFAULT_DAMAGE_STATUS.toString())));
    }

    @Test
    @Transactional
    void getVehicleDamage() throws Exception {
        // Initialize the database
        insertedVehicleDamage = vehicleDamageRepository.saveAndFlush(vehicleDamage);

        // Get the vehicleDamage
        restVehicleDamageMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleDamage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleDamage.getId().intValue()))
            .andExpect(jsonPath("$.damageDescription").value(DEFAULT_DAMAGE_DESCRIPTION))
            .andExpect(jsonPath("$.reportedAt").value(DEFAULT_REPORTED_AT.toString()))
            .andExpect(jsonPath("$.damageStatus").value(DEFAULT_DAMAGE_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVehicleDamage() throws Exception {
        // Get the vehicleDamage
        restVehicleDamageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleDamage() throws Exception {
        // Initialize the database
        insertedVehicleDamage = vehicleDamageRepository.saveAndFlush(vehicleDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleDamage
        VehicleDamage updatedVehicleDamage = vehicleDamageRepository.findById(vehicleDamage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleDamage are not directly saved in db
        em.detach(updatedVehicleDamage);
        updatedVehicleDamage
            .damageDescription(UPDATED_DAMAGE_DESCRIPTION)
            .reportedAt(UPDATED_REPORTED_AT)
            .damageStatus(UPDATED_DAMAGE_STATUS);
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(updatedVehicleDamage);

        restVehicleDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleDamageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleDamageDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleDamageToMatchAllProperties(updatedVehicleDamage);
    }

    @Test
    @Transactional
    void putNonExistingVehicleDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleDamage.setId(longCount.incrementAndGet());

        // Create the VehicleDamage
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleDamageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleDamageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleDamage.setId(longCount.incrementAndGet());

        // Create the VehicleDamage
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleDamageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleDamage.setId(longCount.incrementAndGet());

        // Create the VehicleDamage
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleDamageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDamageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleDamageWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleDamage = vehicleDamageRepository.saveAndFlush(vehicleDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleDamage using partial update
        VehicleDamage partialUpdatedVehicleDamage = new VehicleDamage();
        partialUpdatedVehicleDamage.setId(vehicleDamage.getId());

        partialUpdatedVehicleDamage
            .damageDescription(UPDATED_DAMAGE_DESCRIPTION)
            .reportedAt(UPDATED_REPORTED_AT)
            .damageStatus(UPDATED_DAMAGE_STATUS);

        restVehicleDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleDamage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleDamage))
            )
            .andExpect(status().isOk());

        // Validate the VehicleDamage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleDamageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleDamage, vehicleDamage),
            getPersistedVehicleDamage(vehicleDamage)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleDamageWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleDamage = vehicleDamageRepository.saveAndFlush(vehicleDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleDamage using partial update
        VehicleDamage partialUpdatedVehicleDamage = new VehicleDamage();
        partialUpdatedVehicleDamage.setId(vehicleDamage.getId());

        partialUpdatedVehicleDamage
            .damageDescription(UPDATED_DAMAGE_DESCRIPTION)
            .reportedAt(UPDATED_REPORTED_AT)
            .damageStatus(UPDATED_DAMAGE_STATUS);

        restVehicleDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleDamage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleDamage))
            )
            .andExpect(status().isOk());

        // Validate the VehicleDamage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleDamageUpdatableFieldsEquals(partialUpdatedVehicleDamage, getPersistedVehicleDamage(partialUpdatedVehicleDamage));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleDamage.setId(longCount.incrementAndGet());

        // Create the VehicleDamage
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleDamageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleDamageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleDamage.setId(longCount.incrementAndGet());

        // Create the VehicleDamage
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleDamageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleDamage.setId(longCount.incrementAndGet());

        // Create the VehicleDamage
        VehicleDamageDTO vehicleDamageDTO = vehicleDamageMapper.toDto(vehicleDamage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleDamageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleDamageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleDamage() throws Exception {
        // Initialize the database
        insertedVehicleDamage = vehicleDamageRepository.saveAndFlush(vehicleDamage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleDamage
        restVehicleDamageMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleDamage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleDamageRepository.count();
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

    protected VehicleDamage getPersistedVehicleDamage(VehicleDamage vehicleDamage) {
        return vehicleDamageRepository.findById(vehicleDamage.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleDamageToMatchAllProperties(VehicleDamage expectedVehicleDamage) {
        assertVehicleDamageAllPropertiesEquals(expectedVehicleDamage, getPersistedVehicleDamage(expectedVehicleDamage));
    }

    protected void assertPersistedVehicleDamageToMatchUpdatableProperties(VehicleDamage expectedVehicleDamage) {
        assertVehicleDamageAllUpdatablePropertiesEquals(expectedVehicleDamage, getPersistedVehicleDamage(expectedVehicleDamage));
    }
}
