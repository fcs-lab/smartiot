package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.UserAccountAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.UserAccount;
import br.com.supera.smartiot.domain.enumeration.CNHSituation;
import br.com.supera.smartiot.domain.enumeration.RegisterSituation;
import br.com.supera.smartiot.repository.UserAccountRepository;
import br.com.supera.smartiot.service.dto.UserAccountDTO;
import br.com.supera.smartiot.service.mapper.UserAccountMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link UserAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAccountResourceIT {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "oWb@^bAE(M.wJ\"";
    private static final String UPDATED_EMAIL_ADDRESS = "qdm@MTl.<zkX";

    private static final LocalDate DEFAULT_ADMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADMISSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RPUSH_FEEDBACK_ID = 1;
    private static final Integer UPDATED_RPUSH_FEEDBACK_ID = 2;

    private static final Boolean DEFAULT_EXEC_COMMANDS = false;
    private static final Boolean UPDATED_EXEC_COMMANDS = true;

    private static final Boolean DEFAULT_IS_BLOCKED = false;
    private static final Boolean UPDATED_IS_BLOCKED = true;

    private static final String DEFAULT_EMPLOYER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUSH_CONFIGURATION = 1;
    private static final Integer UPDATED_PUSH_CONFIGURATION = 2;

    private static final Float DEFAULT_TRAVELED_DISTANCE = 1F;
    private static final Float UPDATED_TRAVELED_DISTANCE = 2F;

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_BLOCKED_REASON = "AAAAAAAAAA";
    private static final String UPDATED_BLOCKED_REASON = "BBBBBBBBBB";

    private static final Long DEFAULT_BLOCKED_BY_ID = 1L;
    private static final Long UPDATED_BLOCKED_BY_ID = 2L;

    private static final Instant DEFAULT_BLOCKED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BLOCKED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DELETED_REASON = "AAAAAAAAAA";
    private static final String UPDATED_DELETED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_DELETED_BY_ID = 1L;
    private static final Long UPDATED_DELETED_BY_ID = 2L;

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HINT = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HINT = "BBBBBBBBBB";

    private static final String DEFAULT_FEATURE_FLAGS = "AAAAAAAAAA";
    private static final String UPDATED_FEATURE_FLAGS = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLIC_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PUBLIC_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STREET_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_COMPLEMENT = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_COMPLEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNH_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_CNH_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CNH_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CNH_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final CNHSituation DEFAULT_CNH_STATUS = CNHSituation.VALID;
    private static final CNHSituation UPDATED_CNH_STATUS = CNHSituation.INVALID;

    private static final RegisterSituation DEFAULT_REGISTRATION_STATUS = RegisterSituation.PRE_REGISTRATION;
    private static final RegisterSituation UPDATED_REGISTRATION_STATUS = RegisterSituation.UNDER_ANALYSIS;

    private static final String DEFAULT_ANALYZED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ANALYZED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_ANALYZED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ANALYZED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SIGNATURE_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATURE_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_RESIDENCE_PROOF_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENCE_PROOF_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAccountMockMvc;

    private UserAccount userAccount;

    private UserAccount insertedUserAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createEntity(EntityManager em) {
        UserAccount userAccount = new UserAccount()
            .accountName(DEFAULT_ACCOUNT_NAME)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .admissionDate(DEFAULT_ADMISSION_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .isActive(DEFAULT_IS_ACTIVE)
            .mobilePhone(DEFAULT_MOBILE_PHONE)
            .rpushFeedbackId(DEFAULT_RPUSH_FEEDBACK_ID)
            .execCommands(DEFAULT_EXEC_COMMANDS)
            .isBlocked(DEFAULT_IS_BLOCKED)
            .employerName(DEFAULT_EMPLOYER_NAME)
            .pushConfiguration(DEFAULT_PUSH_CONFIGURATION)
            .traveledDistance(DEFAULT_TRAVELED_DISTANCE)
            .language(DEFAULT_LANGUAGE)
            .blockedReason(DEFAULT_BLOCKED_REASON)
            .blockedById(DEFAULT_BLOCKED_BY_ID)
            .blockedAt(DEFAULT_BLOCKED_AT)
            .deletedReason(DEFAULT_DELETED_REASON)
            .deletedAt(DEFAULT_DELETED_AT)
            .deletedById(DEFAULT_DELETED_BY_ID)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .registrationCode(DEFAULT_REGISTRATION_CODE)
            .password(DEFAULT_PASSWORD)
            .passwordHint(DEFAULT_PASSWORD_HINT)
            .featureFlags(DEFAULT_FEATURE_FLAGS)
            .zipCode(DEFAULT_ZIP_CODE)
            .publicPlace(DEFAULT_PUBLIC_PLACE)
            .addressNumber(DEFAULT_ADDRESS_NUMBER)
            .streetName(DEFAULT_STREET_NAME)
            .addressComplement(DEFAULT_ADDRESS_COMPLEMENT)
            .cityName(DEFAULT_CITY_NAME)
            .stateName(DEFAULT_STATE_NAME)
            .cnhImage(DEFAULT_CNH_IMAGE)
            .profileImage(DEFAULT_PROFILE_IMAGE)
            .cnhExpirationDate(DEFAULT_CNH_EXPIRATION_DATE)
            .cnhStatus(DEFAULT_CNH_STATUS)
            .registrationStatus(DEFAULT_REGISTRATION_STATUS)
            .analyzedBy(DEFAULT_ANALYZED_BY)
            .analyzedAt(DEFAULT_ANALYZED_AT)
            .signatureImage(DEFAULT_SIGNATURE_IMAGE)
            .residenceProofImage(DEFAULT_RESIDENCE_PROOF_IMAGE);
        return userAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccount createUpdatedEntity(EntityManager em) {
        UserAccount userAccount = new UserAccount()
            .accountName(UPDATED_ACCOUNT_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .rpushFeedbackId(UPDATED_RPUSH_FEEDBACK_ID)
            .execCommands(UPDATED_EXEC_COMMANDS)
            .isBlocked(UPDATED_IS_BLOCKED)
            .employerName(UPDATED_EMPLOYER_NAME)
            .pushConfiguration(UPDATED_PUSH_CONFIGURATION)
            .traveledDistance(UPDATED_TRAVELED_DISTANCE)
            .language(UPDATED_LANGUAGE)
            .blockedReason(UPDATED_BLOCKED_REASON)
            .blockedById(UPDATED_BLOCKED_BY_ID)
            .blockedAt(UPDATED_BLOCKED_AT)
            .deletedReason(UPDATED_DELETED_REASON)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedById(UPDATED_DELETED_BY_ID)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .registrationCode(UPDATED_REGISTRATION_CODE)
            .password(UPDATED_PASSWORD)
            .passwordHint(UPDATED_PASSWORD_HINT)
            .featureFlags(UPDATED_FEATURE_FLAGS)
            .zipCode(UPDATED_ZIP_CODE)
            .publicPlace(UPDATED_PUBLIC_PLACE)
            .addressNumber(UPDATED_ADDRESS_NUMBER)
            .streetName(UPDATED_STREET_NAME)
            .addressComplement(UPDATED_ADDRESS_COMPLEMENT)
            .cityName(UPDATED_CITY_NAME)
            .stateName(UPDATED_STATE_NAME)
            .cnhImage(UPDATED_CNH_IMAGE)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .cnhExpirationDate(UPDATED_CNH_EXPIRATION_DATE)
            .cnhStatus(UPDATED_CNH_STATUS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .analyzedBy(UPDATED_ANALYZED_BY)
            .analyzedAt(UPDATED_ANALYZED_AT)
            .signatureImage(UPDATED_SIGNATURE_IMAGE)
            .residenceProofImage(UPDATED_RESIDENCE_PROOF_IMAGE);
        return userAccount;
    }

    @BeforeEach
    public void initTest() {
        userAccount = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserAccount != null) {
            userAccountRepository.delete(insertedUserAccount);
            insertedUserAccount = null;
        }
    }

    @Test
    @Transactional
    void createUserAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);
        var returnedUserAccountDTO = om.readValue(
            restUserAccountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAccountDTO.class
        );

        // Validate the UserAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserAccount = userAccountMapper.toEntity(returnedUserAccountDTO);
        assertUserAccountUpdatableFieldsEquals(returnedUserAccount, getPersistedUserAccount(returnedUserAccount));

        insertedUserAccount = returnedUserAccount;
    }

    @Test
    @Transactional
    void createUserAccountWithExistingId() throws Exception {
        // Create the UserAccount with an existing ID
        userAccount.setId(1L);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAccountNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAccount.setAccountName(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAccount.setEmailAddress(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdmissionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAccount.setAdmissionDate(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAccount.setCreatedAt(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAccount.setUpdatedAt(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAccount.setIsActive(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAccount.setPassword(null);

        // Create the UserAccount, which fails.
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        restUserAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAccounts() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.saveAndFlush(userAccount);

        // Get all the userAccountList
        restUserAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(DEFAULT_ADMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].rpushFeedbackId").value(hasItem(DEFAULT_RPUSH_FEEDBACK_ID)))
            .andExpect(jsonPath("$.[*].execCommands").value(hasItem(DEFAULT_EXEC_COMMANDS.booleanValue())))
            .andExpect(jsonPath("$.[*].isBlocked").value(hasItem(DEFAULT_IS_BLOCKED.booleanValue())))
            .andExpect(jsonPath("$.[*].employerName").value(hasItem(DEFAULT_EMPLOYER_NAME)))
            .andExpect(jsonPath("$.[*].pushConfiguration").value(hasItem(DEFAULT_PUSH_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].traveledDistance").value(hasItem(DEFAULT_TRAVELED_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].blockedReason").value(hasItem(DEFAULT_BLOCKED_REASON)))
            .andExpect(jsonPath("$.[*].blockedById").value(hasItem(DEFAULT_BLOCKED_BY_ID.intValue())))
            .andExpect(jsonPath("$.[*].blockedAt").value(hasItem(DEFAULT_BLOCKED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedReason").value(hasItem(DEFAULT_DELETED_REASON)))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedById").value(hasItem(DEFAULT_DELETED_BY_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].registrationCode").value(hasItem(DEFAULT_REGISTRATION_CODE)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].passwordHint").value(hasItem(DEFAULT_PASSWORD_HINT)))
            .andExpect(jsonPath("$.[*].featureFlags").value(hasItem(DEFAULT_FEATURE_FLAGS)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].publicPlace").value(hasItem(DEFAULT_PUBLIC_PLACE)))
            .andExpect(jsonPath("$.[*].addressNumber").value(hasItem(DEFAULT_ADDRESS_NUMBER)))
            .andExpect(jsonPath("$.[*].streetName").value(hasItem(DEFAULT_STREET_NAME)))
            .andExpect(jsonPath("$.[*].addressComplement").value(hasItem(DEFAULT_ADDRESS_COMPLEMENT)))
            .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME)))
            .andExpect(jsonPath("$.[*].stateName").value(hasItem(DEFAULT_STATE_NAME)))
            .andExpect(jsonPath("$.[*].cnhImage").value(hasItem(DEFAULT_CNH_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(DEFAULT_PROFILE_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].cnhExpirationDate").value(hasItem(DEFAULT_CNH_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].cnhStatus").value(hasItem(DEFAULT_CNH_STATUS.toString())))
            .andExpect(jsonPath("$.[*].registrationStatus").value(hasItem(DEFAULT_REGISTRATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].analyzedBy").value(hasItem(DEFAULT_ANALYZED_BY)))
            .andExpect(jsonPath("$.[*].analyzedAt").value(hasItem(DEFAULT_ANALYZED_AT.toString())))
            .andExpect(jsonPath("$.[*].signatureImage").value(hasItem(DEFAULT_SIGNATURE_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].residenceProofImage").value(hasItem(DEFAULT_RESIDENCE_PROOF_IMAGE.toString())));
    }

    @Test
    @Transactional
    void getUserAccount() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.saveAndFlush(userAccount);

        // Get the userAccount
        restUserAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, userAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.admissionDate").value(DEFAULT_ADMISSION_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE))
            .andExpect(jsonPath("$.rpushFeedbackId").value(DEFAULT_RPUSH_FEEDBACK_ID))
            .andExpect(jsonPath("$.execCommands").value(DEFAULT_EXEC_COMMANDS.booleanValue()))
            .andExpect(jsonPath("$.isBlocked").value(DEFAULT_IS_BLOCKED.booleanValue()))
            .andExpect(jsonPath("$.employerName").value(DEFAULT_EMPLOYER_NAME))
            .andExpect(jsonPath("$.pushConfiguration").value(DEFAULT_PUSH_CONFIGURATION))
            .andExpect(jsonPath("$.traveledDistance").value(DEFAULT_TRAVELED_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.blockedReason").value(DEFAULT_BLOCKED_REASON))
            .andExpect(jsonPath("$.blockedById").value(DEFAULT_BLOCKED_BY_ID.intValue()))
            .andExpect(jsonPath("$.blockedAt").value(DEFAULT_BLOCKED_AT.toString()))
            .andExpect(jsonPath("$.deletedReason").value(DEFAULT_DELETED_REASON))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.deletedById").value(DEFAULT_DELETED_BY_ID.intValue()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.registrationCode").value(DEFAULT_REGISTRATION_CODE))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.passwordHint").value(DEFAULT_PASSWORD_HINT))
            .andExpect(jsonPath("$.featureFlags").value(DEFAULT_FEATURE_FLAGS))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.publicPlace").value(DEFAULT_PUBLIC_PLACE))
            .andExpect(jsonPath("$.addressNumber").value(DEFAULT_ADDRESS_NUMBER))
            .andExpect(jsonPath("$.streetName").value(DEFAULT_STREET_NAME))
            .andExpect(jsonPath("$.addressComplement").value(DEFAULT_ADDRESS_COMPLEMENT))
            .andExpect(jsonPath("$.cityName").value(DEFAULT_CITY_NAME))
            .andExpect(jsonPath("$.stateName").value(DEFAULT_STATE_NAME))
            .andExpect(jsonPath("$.cnhImage").value(DEFAULT_CNH_IMAGE.toString()))
            .andExpect(jsonPath("$.profileImage").value(DEFAULT_PROFILE_IMAGE.toString()))
            .andExpect(jsonPath("$.cnhExpirationDate").value(DEFAULT_CNH_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.cnhStatus").value(DEFAULT_CNH_STATUS.toString()))
            .andExpect(jsonPath("$.registrationStatus").value(DEFAULT_REGISTRATION_STATUS.toString()))
            .andExpect(jsonPath("$.analyzedBy").value(DEFAULT_ANALYZED_BY))
            .andExpect(jsonPath("$.analyzedAt").value(DEFAULT_ANALYZED_AT.toString()))
            .andExpect(jsonPath("$.signatureImage").value(DEFAULT_SIGNATURE_IMAGE.toString()))
            .andExpect(jsonPath("$.residenceProofImage").value(DEFAULT_RESIDENCE_PROOF_IMAGE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserAccount() throws Exception {
        // Get the userAccount
        restUserAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAccount() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.saveAndFlush(userAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount
        UserAccount updatedUserAccount = userAccountRepository.findById(userAccount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAccount are not directly saved in db
        em.detach(updatedUserAccount);
        updatedUserAccount
            .accountName(UPDATED_ACCOUNT_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .rpushFeedbackId(UPDATED_RPUSH_FEEDBACK_ID)
            .execCommands(UPDATED_EXEC_COMMANDS)
            .isBlocked(UPDATED_IS_BLOCKED)
            .employerName(UPDATED_EMPLOYER_NAME)
            .pushConfiguration(UPDATED_PUSH_CONFIGURATION)
            .traveledDistance(UPDATED_TRAVELED_DISTANCE)
            .language(UPDATED_LANGUAGE)
            .blockedReason(UPDATED_BLOCKED_REASON)
            .blockedById(UPDATED_BLOCKED_BY_ID)
            .blockedAt(UPDATED_BLOCKED_AT)
            .deletedReason(UPDATED_DELETED_REASON)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedById(UPDATED_DELETED_BY_ID)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .registrationCode(UPDATED_REGISTRATION_CODE)
            .password(UPDATED_PASSWORD)
            .passwordHint(UPDATED_PASSWORD_HINT)
            .featureFlags(UPDATED_FEATURE_FLAGS)
            .zipCode(UPDATED_ZIP_CODE)
            .publicPlace(UPDATED_PUBLIC_PLACE)
            .addressNumber(UPDATED_ADDRESS_NUMBER)
            .streetName(UPDATED_STREET_NAME)
            .addressComplement(UPDATED_ADDRESS_COMPLEMENT)
            .cityName(UPDATED_CITY_NAME)
            .stateName(UPDATED_STATE_NAME)
            .cnhImage(UPDATED_CNH_IMAGE)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .cnhExpirationDate(UPDATED_CNH_EXPIRATION_DATE)
            .cnhStatus(UPDATED_CNH_STATUS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .analyzedBy(UPDATED_ANALYZED_BY)
            .analyzedAt(UPDATED_ANALYZED_AT)
            .signatureImage(UPDATED_SIGNATURE_IMAGE)
            .residenceProofImage(UPDATED_RESIDENCE_PROOF_IMAGE);
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(updatedUserAccount);

        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAccountToMatchAllProperties(updatedUserAccount);
    }

    @Test
    @Transactional
    void putNonExistingUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.saveAndFlush(userAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount
            .admissionDate(UPDATED_ADMISSION_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .isBlocked(UPDATED_IS_BLOCKED)
            .employerName(UPDATED_EMPLOYER_NAME)
            .traveledDistance(UPDATED_TRAVELED_DISTANCE)
            .deletedAt(UPDATED_DELETED_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .passwordHint(UPDATED_PASSWORD_HINT)
            .featureFlags(UPDATED_FEATURE_FLAGS)
            .zipCode(UPDATED_ZIP_CODE)
            .streetName(UPDATED_STREET_NAME)
            .cnhExpirationDate(UPDATED_CNH_EXPIRATION_DATE)
            .cnhStatus(UPDATED_CNH_STATUS)
            .analyzedBy(UPDATED_ANALYZED_BY)
            .analyzedAt(UPDATED_ANALYZED_AT)
            .signatureImage(UPDATED_SIGNATURE_IMAGE);

        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAccount))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAccount, userAccount),
            getPersistedUserAccount(userAccount)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAccountWithPatch() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.saveAndFlush(userAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccount using partial update
        UserAccount partialUpdatedUserAccount = new UserAccount();
        partialUpdatedUserAccount.setId(userAccount.getId());

        partialUpdatedUserAccount
            .accountName(UPDATED_ACCOUNT_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .rpushFeedbackId(UPDATED_RPUSH_FEEDBACK_ID)
            .execCommands(UPDATED_EXEC_COMMANDS)
            .isBlocked(UPDATED_IS_BLOCKED)
            .employerName(UPDATED_EMPLOYER_NAME)
            .pushConfiguration(UPDATED_PUSH_CONFIGURATION)
            .traveledDistance(UPDATED_TRAVELED_DISTANCE)
            .language(UPDATED_LANGUAGE)
            .blockedReason(UPDATED_BLOCKED_REASON)
            .blockedById(UPDATED_BLOCKED_BY_ID)
            .blockedAt(UPDATED_BLOCKED_AT)
            .deletedReason(UPDATED_DELETED_REASON)
            .deletedAt(UPDATED_DELETED_AT)
            .deletedById(UPDATED_DELETED_BY_ID)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .registrationCode(UPDATED_REGISTRATION_CODE)
            .password(UPDATED_PASSWORD)
            .passwordHint(UPDATED_PASSWORD_HINT)
            .featureFlags(UPDATED_FEATURE_FLAGS)
            .zipCode(UPDATED_ZIP_CODE)
            .publicPlace(UPDATED_PUBLIC_PLACE)
            .addressNumber(UPDATED_ADDRESS_NUMBER)
            .streetName(UPDATED_STREET_NAME)
            .addressComplement(UPDATED_ADDRESS_COMPLEMENT)
            .cityName(UPDATED_CITY_NAME)
            .stateName(UPDATED_STATE_NAME)
            .cnhImage(UPDATED_CNH_IMAGE)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .cnhExpirationDate(UPDATED_CNH_EXPIRATION_DATE)
            .cnhStatus(UPDATED_CNH_STATUS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .analyzedBy(UPDATED_ANALYZED_BY)
            .analyzedAt(UPDATED_ANALYZED_AT)
            .signatureImage(UPDATED_SIGNATURE_IMAGE)
            .residenceProofImage(UPDATED_RESIDENCE_PROOF_IMAGE);

        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAccount))
            )
            .andExpect(status().isOk());

        // Validate the UserAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAccountUpdatableFieldsEquals(partialUpdatedUserAccount, getPersistedUserAccount(partialUpdatedUserAccount));
    }

    @Test
    @Transactional
    void patchNonExistingUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccount.setId(longCount.incrementAndGet());

        // Create the UserAccount
        UserAccountDTO userAccountDTO = userAccountMapper.toDto(userAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAccount() throws Exception {
        // Initialize the database
        insertedUserAccount = userAccountRepository.saveAndFlush(userAccount);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAccount
        restUserAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAccountRepository.count();
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

    protected UserAccount getPersistedUserAccount(UserAccount userAccount) {
        return userAccountRepository.findById(userAccount.getId()).orElseThrow();
    }

    protected void assertPersistedUserAccountToMatchAllProperties(UserAccount expectedUserAccount) {
        assertUserAccountAllPropertiesEquals(expectedUserAccount, getPersistedUserAccount(expectedUserAccount));
    }

    protected void assertPersistedUserAccountToMatchUpdatableProperties(UserAccount expectedUserAccount) {
        assertUserAccountAllUpdatablePropertiesEquals(expectedUserAccount, getPersistedUserAccount(expectedUserAccount));
    }
}
