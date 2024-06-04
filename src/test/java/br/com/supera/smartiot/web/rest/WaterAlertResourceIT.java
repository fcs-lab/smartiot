package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.WaterAlertAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.smartiot.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.WaterAlert;
import br.com.supera.smartiot.repository.WaterAlertRepository;
import br.com.supera.smartiot.service.dto.WaterAlertDTO;
import br.com.supera.smartiot.service.mapper.WaterAlertMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link WaterAlertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WaterAlertResourceIT {

    private static final String DEFAULT_ALERT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ALERT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ALERT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ALERT_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/water-alerts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WaterAlertRepository waterAlertRepository;

    @Autowired
    private WaterAlertMapper waterAlertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWaterAlertMockMvc;

    private WaterAlert waterAlert;

    private WaterAlert insertedWaterAlert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaterAlert createEntity(EntityManager em) {
        WaterAlert waterAlert = new WaterAlert()
            .alertType(DEFAULT_ALERT_TYPE)
            .alertDescription(DEFAULT_ALERT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE);
        return waterAlert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaterAlert createUpdatedEntity(EntityManager em) {
        WaterAlert waterAlert = new WaterAlert()
            .alertType(UPDATED_ALERT_TYPE)
            .alertDescription(UPDATED_ALERT_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE);
        return waterAlert;
    }

    @BeforeEach
    public void initTest() {
        waterAlert = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedWaterAlert != null) {
            waterAlertRepository.delete(insertedWaterAlert);
            insertedWaterAlert = null;
        }
    }

    @Test
    @Transactional
    void createWaterAlert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WaterAlert
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);
        var returnedWaterAlertDTO = om.readValue(
            restWaterAlertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterAlertDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WaterAlertDTO.class
        );

        // Validate the WaterAlert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWaterAlert = waterAlertMapper.toEntity(returnedWaterAlertDTO);
        assertWaterAlertUpdatableFieldsEquals(returnedWaterAlert, getPersistedWaterAlert(returnedWaterAlert));

        insertedWaterAlert = returnedWaterAlert;
    }

    @Test
    @Transactional
    void createWaterAlertWithExistingId() throws Exception {
        // Create the WaterAlert with an existing ID
        waterAlert.setId(1L);
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaterAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterAlertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WaterAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAlertTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterAlert.setAlertType(null);

        // Create the WaterAlert, which fails.
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        restWaterAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlertDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterAlert.setAlertDescription(null);

        // Create the WaterAlert, which fails.
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        restWaterAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        waterAlert.setCreatedDate(null);

        // Create the WaterAlert, which fails.
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        restWaterAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWaterAlerts() throws Exception {
        // Initialize the database
        insertedWaterAlert = waterAlertRepository.saveAndFlush(waterAlert);

        // Get all the waterAlertList
        restWaterAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waterAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].alertType").value(hasItem(DEFAULT_ALERT_TYPE)))
            .andExpect(jsonPath("$.[*].alertDescription").value(hasItem(DEFAULT_ALERT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }

    @Test
    @Transactional
    void getWaterAlert() throws Exception {
        // Initialize the database
        insertedWaterAlert = waterAlertRepository.saveAndFlush(waterAlert);

        // Get the waterAlert
        restWaterAlertMockMvc
            .perform(get(ENTITY_API_URL_ID, waterAlert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waterAlert.getId().intValue()))
            .andExpect(jsonPath("$.alertType").value(DEFAULT_ALERT_TYPE))
            .andExpect(jsonPath("$.alertDescription").value(DEFAULT_ALERT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingWaterAlert() throws Exception {
        // Get the waterAlert
        restWaterAlertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWaterAlert() throws Exception {
        // Initialize the database
        insertedWaterAlert = waterAlertRepository.saveAndFlush(waterAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterAlert
        WaterAlert updatedWaterAlert = waterAlertRepository.findById(waterAlert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWaterAlert are not directly saved in db
        em.detach(updatedWaterAlert);
        updatedWaterAlert.alertType(UPDATED_ALERT_TYPE).alertDescription(UPDATED_ALERT_DESCRIPTION).createdDate(UPDATED_CREATED_DATE);
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(updatedWaterAlert);

        restWaterAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waterAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterAlertDTO))
            )
            .andExpect(status().isOk());

        // Validate the WaterAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWaterAlertToMatchAllProperties(updatedWaterAlert);
    }

    @Test
    @Transactional
    void putNonExistingWaterAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterAlert.setId(longCount.incrementAndGet());

        // Create the WaterAlert
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaterAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waterAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWaterAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterAlert.setId(longCount.incrementAndGet());

        // Create the WaterAlert
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(waterAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWaterAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterAlert.setId(longCount.incrementAndGet());

        // Create the WaterAlert
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterAlertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(waterAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaterAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWaterAlertWithPatch() throws Exception {
        // Initialize the database
        insertedWaterAlert = waterAlertRepository.saveAndFlush(waterAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterAlert using partial update
        WaterAlert partialUpdatedWaterAlert = new WaterAlert();
        partialUpdatedWaterAlert.setId(waterAlert.getId());

        partialUpdatedWaterAlert.createdDate(UPDATED_CREATED_DATE);

        restWaterAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaterAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWaterAlert))
            )
            .andExpect(status().isOk());

        // Validate the WaterAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWaterAlertUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWaterAlert, waterAlert),
            getPersistedWaterAlert(waterAlert)
        );
    }

    @Test
    @Transactional
    void fullUpdateWaterAlertWithPatch() throws Exception {
        // Initialize the database
        insertedWaterAlert = waterAlertRepository.saveAndFlush(waterAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the waterAlert using partial update
        WaterAlert partialUpdatedWaterAlert = new WaterAlert();
        partialUpdatedWaterAlert.setId(waterAlert.getId());

        partialUpdatedWaterAlert
            .alertType(UPDATED_ALERT_TYPE)
            .alertDescription(UPDATED_ALERT_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE);

        restWaterAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaterAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWaterAlert))
            )
            .andExpect(status().isOk());

        // Validate the WaterAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWaterAlertUpdatableFieldsEquals(partialUpdatedWaterAlert, getPersistedWaterAlert(partialUpdatedWaterAlert));
    }

    @Test
    @Transactional
    void patchNonExistingWaterAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterAlert.setId(longCount.incrementAndGet());

        // Create the WaterAlert
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaterAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, waterAlertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(waterAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWaterAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterAlert.setId(longCount.incrementAndGet());

        // Create the WaterAlert
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(waterAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaterAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWaterAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        waterAlert.setId(longCount.incrementAndGet());

        // Create the WaterAlert
        WaterAlertDTO waterAlertDTO = waterAlertMapper.toDto(waterAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaterAlertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(waterAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaterAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWaterAlert() throws Exception {
        // Initialize the database
        insertedWaterAlert = waterAlertRepository.saveAndFlush(waterAlert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the waterAlert
        restWaterAlertMockMvc
            .perform(delete(ENTITY_API_URL_ID, waterAlert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return waterAlertRepository.count();
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

    protected WaterAlert getPersistedWaterAlert(WaterAlert waterAlert) {
        return waterAlertRepository.findById(waterAlert.getId()).orElseThrow();
    }

    protected void assertPersistedWaterAlertToMatchAllProperties(WaterAlert expectedWaterAlert) {
        assertWaterAlertAllPropertiesEquals(expectedWaterAlert, getPersistedWaterAlert(expectedWaterAlert));
    }

    protected void assertPersistedWaterAlertToMatchUpdatableProperties(WaterAlert expectedWaterAlert) {
        assertWaterAlertAllUpdatablePropertiesEquals(expectedWaterAlert, getPersistedWaterAlert(expectedWaterAlert));
    }
}
