package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.SystemAlertAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.SystemAlert;
import br.com.supera.smartiot.repository.SystemAlertRepository;
import br.com.supera.smartiot.service.dto.SystemAlertDTO;
import br.com.supera.smartiot.service.mapper.SystemAlertMapper;
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
 * Integration tests for the {@link SystemAlertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemAlertResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ALERT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ALERT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ALERT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ALERT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/system-alerts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemAlertRepository systemAlertRepository;

    @Autowired
    private SystemAlertMapper systemAlertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemAlertMockMvc;

    private SystemAlert systemAlert;

    private SystemAlert insertedSystemAlert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemAlert createEntity(EntityManager em) {
        SystemAlert systemAlert = new SystemAlert()
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .alertDescription(DEFAULT_ALERT_DESCRIPTION)
            .alertType(DEFAULT_ALERT_TYPE);
        return systemAlert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemAlert createUpdatedEntity(EntityManager em) {
        SystemAlert systemAlert = new SystemAlert()
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .alertDescription(UPDATED_ALERT_DESCRIPTION)
            .alertType(UPDATED_ALERT_TYPE);
        return systemAlert;
    }

    @BeforeEach
    public void initTest() {
        systemAlert = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSystemAlert != null) {
            systemAlertRepository.delete(insertedSystemAlert);
            insertedSystemAlert = null;
        }
    }

    @Test
    @Transactional
    void createSystemAlert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemAlert
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);
        var returnedSystemAlertDTO = om.readValue(
            restSystemAlertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemAlertDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemAlertDTO.class
        );

        // Validate the SystemAlert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemAlert = systemAlertMapper.toEntity(returnedSystemAlertDTO);
        assertSystemAlertUpdatableFieldsEquals(returnedSystemAlert, getPersistedSystemAlert(returnedSystemAlert));

        insertedSystemAlert = returnedSystemAlert;
    }

    @Test
    @Transactional
    void createSystemAlertWithExistingId() throws Exception {
        // Create the SystemAlert with an existing ID
        systemAlert.setId(1L);
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemAlertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemAlert.setCreatedAt(null);

        // Create the SystemAlert, which fails.
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        restSystemAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlertDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemAlert.setAlertDescription(null);

        // Create the SystemAlert, which fails.
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        restSystemAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlertTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemAlert.setAlertType(null);

        // Create the SystemAlert, which fails.
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        restSystemAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemAlerts() throws Exception {
        // Initialize the database
        insertedSystemAlert = systemAlertRepository.saveAndFlush(systemAlert);

        // Get all the systemAlertList
        restSystemAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].alertDescription").value(hasItem(DEFAULT_ALERT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].alertType").value(hasItem(DEFAULT_ALERT_TYPE)));
    }

    @Test
    @Transactional
    void getSystemAlert() throws Exception {
        // Initialize the database
        insertedSystemAlert = systemAlertRepository.saveAndFlush(systemAlert);

        // Get the systemAlert
        restSystemAlertMockMvc
            .perform(get(ENTITY_API_URL_ID, systemAlert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemAlert.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.alertDescription").value(DEFAULT_ALERT_DESCRIPTION))
            .andExpect(jsonPath("$.alertType").value(DEFAULT_ALERT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingSystemAlert() throws Exception {
        // Get the systemAlert
        restSystemAlertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemAlert() throws Exception {
        // Initialize the database
        insertedSystemAlert = systemAlertRepository.saveAndFlush(systemAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemAlert
        SystemAlert updatedSystemAlert = systemAlertRepository.findById(systemAlert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemAlert are not directly saved in db
        em.detach(updatedSystemAlert);
        updatedSystemAlert
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .alertDescription(UPDATED_ALERT_DESCRIPTION)
            .alertType(UPDATED_ALERT_TYPE);
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(updatedSystemAlert);

        restSystemAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemAlertDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemAlertToMatchAllProperties(updatedSystemAlert);
    }

    @Test
    @Transactional
    void putNonExistingSystemAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemAlert.setId(longCount.incrementAndGet());

        // Create the SystemAlert
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemAlert.setId(longCount.incrementAndGet());

        // Create the SystemAlert
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemAlert.setId(longCount.incrementAndGet());

        // Create the SystemAlert
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemAlertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemAlertWithPatch() throws Exception {
        // Initialize the database
        insertedSystemAlert = systemAlertRepository.saveAndFlush(systemAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemAlert using partial update
        SystemAlert partialUpdatedSystemAlert = new SystemAlert();
        partialUpdatedSystemAlert.setId(systemAlert.getId());

        partialUpdatedSystemAlert.createdAt(UPDATED_CREATED_AT);

        restSystemAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemAlert))
            )
            .andExpect(status().isOk());

        // Validate the SystemAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemAlertUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemAlert, systemAlert),
            getPersistedSystemAlert(systemAlert)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemAlertWithPatch() throws Exception {
        // Initialize the database
        insertedSystemAlert = systemAlertRepository.saveAndFlush(systemAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemAlert using partial update
        SystemAlert partialUpdatedSystemAlert = new SystemAlert();
        partialUpdatedSystemAlert.setId(systemAlert.getId());

        partialUpdatedSystemAlert
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .alertDescription(UPDATED_ALERT_DESCRIPTION)
            .alertType(UPDATED_ALERT_TYPE);

        restSystemAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemAlert))
            )
            .andExpect(status().isOk());

        // Validate the SystemAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemAlertUpdatableFieldsEquals(partialUpdatedSystemAlert, getPersistedSystemAlert(partialUpdatedSystemAlert));
    }

    @Test
    @Transactional
    void patchNonExistingSystemAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemAlert.setId(longCount.incrementAndGet());

        // Create the SystemAlert
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemAlertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemAlert.setId(longCount.incrementAndGet());

        // Create the SystemAlert
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemAlert.setId(longCount.incrementAndGet());

        // Create the SystemAlert
        SystemAlertDTO systemAlertDTO = systemAlertMapper.toDto(systemAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemAlertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemAlert() throws Exception {
        // Initialize the database
        insertedSystemAlert = systemAlertRepository.saveAndFlush(systemAlert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemAlert
        restSystemAlertMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemAlert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemAlertRepository.count();
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

    protected SystemAlert getPersistedSystemAlert(SystemAlert systemAlert) {
        return systemAlertRepository.findById(systemAlert.getId()).orElseThrow();
    }

    protected void assertPersistedSystemAlertToMatchAllProperties(SystemAlert expectedSystemAlert) {
        assertSystemAlertAllPropertiesEquals(expectedSystemAlert, getPersistedSystemAlert(expectedSystemAlert));
    }

    protected void assertPersistedSystemAlertToMatchUpdatableProperties(SystemAlert expectedSystemAlert) {
        assertSystemAlertAllUpdatablePropertiesEquals(expectedSystemAlert, getPersistedSystemAlert(expectedSystemAlert));
    }
}
