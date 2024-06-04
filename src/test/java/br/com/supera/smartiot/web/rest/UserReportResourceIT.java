package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.UserReportAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.UserReport;
import br.com.supera.smartiot.repository.UserReportRepository;
import br.com.supera.smartiot.service.dto.UserReportDTO;
import br.com.supera.smartiot.service.mapper.UserReportMapper;
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
 * Integration tests for the {@link UserReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserReportResourceIT {

    private static final String DEFAULT_REPORT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_GENERATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GENERATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REPORT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserReportRepository userReportRepository;

    @Autowired
    private UserReportMapper userReportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserReportMockMvc;

    private UserReport userReport;

    private UserReport insertedUserReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserReport createEntity(EntityManager em) {
        UserReport userReport = new UserReport()
            .reportType(DEFAULT_REPORT_TYPE)
            .generatedAt(DEFAULT_GENERATED_AT)
            .reportContent(DEFAULT_REPORT_CONTENT);
        return userReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserReport createUpdatedEntity(EntityManager em) {
        UserReport userReport = new UserReport()
            .reportType(UPDATED_REPORT_TYPE)
            .generatedAt(UPDATED_GENERATED_AT)
            .reportContent(UPDATED_REPORT_CONTENT);
        return userReport;
    }

    @BeforeEach
    public void initTest() {
        userReport = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserReport != null) {
            userReportRepository.delete(insertedUserReport);
            insertedUserReport = null;
        }
    }

    @Test
    @Transactional
    void createUserReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);
        var returnedUserReportDTO = om.readValue(
            restUserReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserReportDTO.class
        );

        // Validate the UserReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserReport = userReportMapper.toEntity(returnedUserReportDTO);
        assertUserReportUpdatableFieldsEquals(returnedUserReport, getPersistedUserReport(returnedUserReport));

        insertedUserReport = returnedUserReport;
    }

    @Test
    @Transactional
    void createUserReportWithExistingId() throws Exception {
        // Create the UserReport with an existing ID
        userReport.setId(1L);
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReportTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userReport.setReportType(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGeneratedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userReport.setGeneratedAt(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserReports() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        // Get all the userReportList
        restUserReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportType").value(hasItem(DEFAULT_REPORT_TYPE)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(DEFAULT_GENERATED_AT.toString())))
            .andExpect(jsonPath("$.[*].reportContent").value(hasItem(DEFAULT_REPORT_CONTENT.toString())));
    }

    @Test
    @Transactional
    void getUserReport() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        // Get the userReport
        restUserReportMockMvc
            .perform(get(ENTITY_API_URL_ID, userReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userReport.getId().intValue()))
            .andExpect(jsonPath("$.reportType").value(DEFAULT_REPORT_TYPE))
            .andExpect(jsonPath("$.generatedAt").value(DEFAULT_GENERATED_AT.toString()))
            .andExpect(jsonPath("$.reportContent").value(DEFAULT_REPORT_CONTENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserReport() throws Exception {
        // Get the userReport
        restUserReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserReport() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userReport
        UserReport updatedUserReport = userReportRepository.findById(userReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserReport are not directly saved in db
        em.detach(updatedUserReport);
        updatedUserReport.reportType(UPDATED_REPORT_TYPE).generatedAt(UPDATED_GENERATED_AT).reportContent(UPDATED_REPORT_CONTENT);
        UserReportDTO userReportDTO = userReportMapper.toDto(updatedUserReport);

        restUserReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserReportToMatchAllProperties(updatedUserReport);
    }

    @Test
    @Transactional
    void putNonExistingUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserReportWithPatch() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userReport using partial update
        UserReport partialUpdatedUserReport = new UserReport();
        partialUpdatedUserReport.setId(userReport.getId());

        partialUpdatedUserReport.reportType(UPDATED_REPORT_TYPE).reportContent(UPDATED_REPORT_CONTENT);

        restUserReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserReport))
            )
            .andExpect(status().isOk());

        // Validate the UserReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserReport, userReport),
            getPersistedUserReport(userReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserReportWithPatch() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userReport using partial update
        UserReport partialUpdatedUserReport = new UserReport();
        partialUpdatedUserReport.setId(userReport.getId());

        partialUpdatedUserReport.reportType(UPDATED_REPORT_TYPE).generatedAt(UPDATED_GENERATED_AT).reportContent(UPDATED_REPORT_CONTENT);

        restUserReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserReport))
            )
            .andExpect(status().isOk());

        // Validate the UserReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserReportUpdatableFieldsEquals(partialUpdatedUserReport, getPersistedUserReport(partialUpdatedUserReport));
    }

    @Test
    @Transactional
    void patchNonExistingUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserReport() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userReport
        restUserReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, userReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userReportRepository.count();
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

    protected UserReport getPersistedUserReport(UserReport userReport) {
        return userReportRepository.findById(userReport.getId()).orElseThrow();
    }

    protected void assertPersistedUserReportToMatchAllProperties(UserReport expectedUserReport) {
        assertUserReportAllPropertiesEquals(expectedUserReport, getPersistedUserReport(expectedUserReport));
    }

    protected void assertPersistedUserReportToMatchUpdatableProperties(UserReport expectedUserReport) {
        assertUserReportAllUpdatablePropertiesEquals(expectedUserReport, getPersistedUserReport(expectedUserReport));
    }
}
