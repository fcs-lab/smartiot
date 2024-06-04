package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.UserDashboardAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.UserDashboard;
import br.com.supera.smartiot.repository.UserDashboardRepository;
import br.com.supera.smartiot.service.dto.UserDashboardDTO;
import br.com.supera.smartiot.service.mapper.UserDashboardMapper;
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
 * Integration tests for the {@link UserDashboardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserDashboardResourceIT {

    private static final String DEFAULT_DASHBOARD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DASHBOARD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WIDGETS = "AAAAAAAAAA";
    private static final String UPDATED_WIDGETS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-dashboards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserDashboardRepository userDashboardRepository;

    @Autowired
    private UserDashboardMapper userDashboardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDashboardMockMvc;

    private UserDashboard userDashboard;

    private UserDashboard insertedUserDashboard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDashboard createEntity(EntityManager em) {
        UserDashboard userDashboard = new UserDashboard().dashboardName(DEFAULT_DASHBOARD_NAME).widgets(DEFAULT_WIDGETS);
        return userDashboard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDashboard createUpdatedEntity(EntityManager em) {
        UserDashboard userDashboard = new UserDashboard().dashboardName(UPDATED_DASHBOARD_NAME).widgets(UPDATED_WIDGETS);
        return userDashboard;
    }

    @BeforeEach
    public void initTest() {
        userDashboard = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserDashboard != null) {
            userDashboardRepository.delete(insertedUserDashboard);
            insertedUserDashboard = null;
        }
    }

    @Test
    @Transactional
    void createUserDashboard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserDashboard
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);
        var returnedUserDashboardDTO = om.readValue(
            restUserDashboardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDashboardDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserDashboardDTO.class
        );

        // Validate the UserDashboard in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserDashboard = userDashboardMapper.toEntity(returnedUserDashboardDTO);
        assertUserDashboardUpdatableFieldsEquals(returnedUserDashboard, getPersistedUserDashboard(returnedUserDashboard));

        insertedUserDashboard = returnedUserDashboard;
    }

    @Test
    @Transactional
    void createUserDashboardWithExistingId() throws Exception {
        // Create the UserDashboard with an existing ID
        userDashboard.setId(1L);
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDashboardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDashboardNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userDashboard.setDashboardName(null);

        // Create the UserDashboard, which fails.
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);

        restUserDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDashboardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserDashboards() throws Exception {
        // Initialize the database
        insertedUserDashboard = userDashboardRepository.saveAndFlush(userDashboard);

        // Get all the userDashboardList
        restUserDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDashboard.getId().intValue())))
            .andExpect(jsonPath("$.[*].dashboardName").value(hasItem(DEFAULT_DASHBOARD_NAME)))
            .andExpect(jsonPath("$.[*].widgets").value(hasItem(DEFAULT_WIDGETS.toString())));
    }

    @Test
    @Transactional
    void getUserDashboard() throws Exception {
        // Initialize the database
        insertedUserDashboard = userDashboardRepository.saveAndFlush(userDashboard);

        // Get the userDashboard
        restUserDashboardMockMvc
            .perform(get(ENTITY_API_URL_ID, userDashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDashboard.getId().intValue()))
            .andExpect(jsonPath("$.dashboardName").value(DEFAULT_DASHBOARD_NAME))
            .andExpect(jsonPath("$.widgets").value(DEFAULT_WIDGETS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserDashboard() throws Exception {
        // Get the userDashboard
        restUserDashboardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserDashboard() throws Exception {
        // Initialize the database
        insertedUserDashboard = userDashboardRepository.saveAndFlush(userDashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDashboard
        UserDashboard updatedUserDashboard = userDashboardRepository.findById(userDashboard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserDashboard are not directly saved in db
        em.detach(updatedUserDashboard);
        updatedUserDashboard.dashboardName(UPDATED_DASHBOARD_NAME).widgets(UPDATED_WIDGETS);
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(updatedUserDashboard);

        restUserDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDashboardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDashboardDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserDashboardToMatchAllProperties(updatedUserDashboard);
    }

    @Test
    @Transactional
    void putNonExistingUserDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDashboard.setId(longCount.incrementAndGet());

        // Create the UserDashboard
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDashboardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDashboard.setId(longCount.incrementAndGet());

        // Create the UserDashboard
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDashboard.setId(longCount.incrementAndGet());

        // Create the UserDashboard
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDashboardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDashboardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserDashboardWithPatch() throws Exception {
        // Initialize the database
        insertedUserDashboard = userDashboardRepository.saveAndFlush(userDashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDashboard using partial update
        UserDashboard partialUpdatedUserDashboard = new UserDashboard();
        partialUpdatedUserDashboard.setId(userDashboard.getId());

        partialUpdatedUserDashboard.dashboardName(UPDATED_DASHBOARD_NAME);

        restUserDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDashboard))
            )
            .andExpect(status().isOk());

        // Validate the UserDashboard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDashboardUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserDashboard, userDashboard),
            getPersistedUserDashboard(userDashboard)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserDashboardWithPatch() throws Exception {
        // Initialize the database
        insertedUserDashboard = userDashboardRepository.saveAndFlush(userDashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDashboard using partial update
        UserDashboard partialUpdatedUserDashboard = new UserDashboard();
        partialUpdatedUserDashboard.setId(userDashboard.getId());

        partialUpdatedUserDashboard.dashboardName(UPDATED_DASHBOARD_NAME).widgets(UPDATED_WIDGETS);

        restUserDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDashboard))
            )
            .andExpect(status().isOk());

        // Validate the UserDashboard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDashboardUpdatableFieldsEquals(partialUpdatedUserDashboard, getPersistedUserDashboard(partialUpdatedUserDashboard));
    }

    @Test
    @Transactional
    void patchNonExistingUserDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDashboard.setId(longCount.incrementAndGet());

        // Create the UserDashboard
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDashboardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDashboard.setId(longCount.incrementAndGet());

        // Create the UserDashboard
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDashboard.setId(longCount.incrementAndGet());

        // Create the UserDashboard
        UserDashboardDTO userDashboardDTO = userDashboardMapper.toDto(userDashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDashboardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userDashboardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserDashboard() throws Exception {
        // Initialize the database
        insertedUserDashboard = userDashboardRepository.saveAndFlush(userDashboard);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userDashboard
        restUserDashboardMockMvc
            .perform(delete(ENTITY_API_URL_ID, userDashboard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userDashboardRepository.count();
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

    protected UserDashboard getPersistedUserDashboard(UserDashboard userDashboard) {
        return userDashboardRepository.findById(userDashboard.getId()).orElseThrow();
    }

    protected void assertPersistedUserDashboardToMatchAllProperties(UserDashboard expectedUserDashboard) {
        assertUserDashboardAllPropertiesEquals(expectedUserDashboard, getPersistedUserDashboard(expectedUserDashboard));
    }

    protected void assertPersistedUserDashboardToMatchUpdatableProperties(UserDashboard expectedUserDashboard) {
        assertUserDashboardAllUpdatablePropertiesEquals(expectedUserDashboard, getPersistedUserDashboard(expectedUserDashboard));
    }
}
