package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.UserContractAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.UserContract;
import br.com.supera.smartiot.repository.UserContractRepository;
import br.com.supera.smartiot.service.UserContractService;
import br.com.supera.smartiot.service.dto.UserContractDTO;
import br.com.supera.smartiot.service.mapper.UserContractMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserContractResourceIT {

    private static final String DEFAULT_CONTRACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserContractRepository userContractRepository;

    @Mock
    private UserContractRepository userContractRepositoryMock;

    @Autowired
    private UserContractMapper userContractMapper;

    @Mock
    private UserContractService userContractServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserContractMockMvc;

    private UserContract userContract;

    private UserContract insertedUserContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserContract createEntity(EntityManager em) {
        UserContract userContract = new UserContract()
            .contractName(DEFAULT_CONTRACT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return userContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserContract createUpdatedEntity(EntityManager em) {
        UserContract userContract = new UserContract()
            .contractName(UPDATED_CONTRACT_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return userContract;
    }

    @BeforeEach
    public void initTest() {
        userContract = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserContract != null) {
            userContractRepository.delete(insertedUserContract);
            insertedUserContract = null;
        }
    }

    @Test
    @Transactional
    void createUserContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserContract
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);
        var returnedUserContractDTO = om.readValue(
            restUserContractMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userContractDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserContractDTO.class
        );

        // Validate the UserContract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserContract = userContractMapper.toEntity(returnedUserContractDTO);
        assertUserContractUpdatableFieldsEquals(returnedUserContract, getPersistedUserContract(returnedUserContract));

        insertedUserContract = returnedUserContract;
    }

    @Test
    @Transactional
    void createUserContractWithExistingId() throws Exception {
        // Create the UserContract with an existing ID
        userContract.setId(1L);
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userContractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserContract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContractNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userContract.setContractName(null);

        // Create the UserContract, which fails.
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        restUserContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userContractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userContract.setStartDate(null);

        // Create the UserContract, which fails.
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        restUserContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userContractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userContract.setEndDate(null);

        // Create the UserContract, which fails.
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        restUserContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userContractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserContracts() throws Exception {
        // Initialize the database
        insertedUserContract = userContractRepository.saveAndFlush(userContract);

        // Get all the userContractList
        restUserContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractName").value(hasItem(DEFAULT_CONTRACT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserContractsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userContractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userContractServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserContractsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userContractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userContractRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserContract() throws Exception {
        // Initialize the database
        insertedUserContract = userContractRepository.saveAndFlush(userContract);

        // Get the userContract
        restUserContractMockMvc
            .perform(get(ENTITY_API_URL_ID, userContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userContract.getId().intValue()))
            .andExpect(jsonPath("$.contractName").value(DEFAULT_CONTRACT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserContract() throws Exception {
        // Get the userContract
        restUserContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserContract() throws Exception {
        // Initialize the database
        insertedUserContract = userContractRepository.saveAndFlush(userContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userContract
        UserContract updatedUserContract = userContractRepository.findById(userContract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserContract are not directly saved in db
        em.detach(updatedUserContract);
        updatedUserContract.contractName(UPDATED_CONTRACT_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);
        UserContractDTO userContractDTO = userContractMapper.toDto(updatedUserContract);

        restUserContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userContractDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserContractToMatchAllProperties(updatedUserContract);
    }

    @Test
    @Transactional
    void putNonExistingUserContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userContract.setId(longCount.incrementAndGet());

        // Create the UserContract
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userContract.setId(longCount.incrementAndGet());

        // Create the UserContract
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userContract.setId(longCount.incrementAndGet());

        // Create the UserContract
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userContractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserContractWithPatch() throws Exception {
        // Initialize the database
        insertedUserContract = userContractRepository.saveAndFlush(userContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userContract using partial update
        UserContract partialUpdatedUserContract = new UserContract();
        partialUpdatedUserContract.setId(userContract.getId());

        partialUpdatedUserContract.contractName(UPDATED_CONTRACT_NAME);

        restUserContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserContract))
            )
            .andExpect(status().isOk());

        // Validate the UserContract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserContractUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserContract, userContract),
            getPersistedUserContract(userContract)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserContractWithPatch() throws Exception {
        // Initialize the database
        insertedUserContract = userContractRepository.saveAndFlush(userContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userContract using partial update
        UserContract partialUpdatedUserContract = new UserContract();
        partialUpdatedUserContract.setId(userContract.getId());

        partialUpdatedUserContract.contractName(UPDATED_CONTRACT_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restUserContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserContract))
            )
            .andExpect(status().isOk());

        // Validate the UserContract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserContractUpdatableFieldsEquals(partialUpdatedUserContract, getPersistedUserContract(partialUpdatedUserContract));
    }

    @Test
    @Transactional
    void patchNonExistingUserContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userContract.setId(longCount.incrementAndGet());

        // Create the UserContract
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userContractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userContract.setId(longCount.incrementAndGet());

        // Create the UserContract
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userContract.setId(longCount.incrementAndGet());

        // Create the UserContract
        UserContractDTO userContractDTO = userContractMapper.toDto(userContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userContractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserContract() throws Exception {
        // Initialize the database
        insertedUserContract = userContractRepository.saveAndFlush(userContract);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userContract
        restUserContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, userContract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userContractRepository.count();
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

    protected UserContract getPersistedUserContract(UserContract userContract) {
        return userContractRepository.findById(userContract.getId()).orElseThrow();
    }

    protected void assertPersistedUserContractToMatchAllProperties(UserContract expectedUserContract) {
        assertUserContractAllPropertiesEquals(expectedUserContract, getPersistedUserContract(expectedUserContract));
    }

    protected void assertPersistedUserContractToMatchUpdatableProperties(UserContract expectedUserContract) {
        assertUserContractAllUpdatablePropertiesEquals(expectedUserContract, getPersistedUserContract(expectedUserContract));
    }
}
