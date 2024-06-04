package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ChatUserAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.ChatUser;
import br.com.supera.smartiot.repository.ChatUserRepository;
import br.com.supera.smartiot.service.dto.ChatUserDTO;
import br.com.supera.smartiot.service.mapper.ChatUserMapper;
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
 * Integration tests for the {@link ChatUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatUserResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chat-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private ChatUserMapper chatUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatUserMockMvc;

    private ChatUser chatUser;

    private ChatUser insertedChatUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatUser createEntity(EntityManager em) {
        ChatUser chatUser = new ChatUser().userId(DEFAULT_USER_ID).userName(DEFAULT_USER_NAME);
        return chatUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatUser createUpdatedEntity(EntityManager em) {
        ChatUser chatUser = new ChatUser().userId(UPDATED_USER_ID).userName(UPDATED_USER_NAME);
        return chatUser;
    }

    @BeforeEach
    public void initTest() {
        chatUser = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedChatUser != null) {
            chatUserRepository.delete(insertedChatUser);
            insertedChatUser = null;
        }
    }

    @Test
    @Transactional
    void createChatUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);
        var returnedChatUserDTO = om.readValue(
            restChatUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChatUserDTO.class
        );

        // Validate the ChatUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChatUser = chatUserMapper.toEntity(returnedChatUserDTO);
        assertChatUserUpdatableFieldsEquals(returnedChatUser, getPersistedChatUser(returnedChatUser));

        insertedChatUser = returnedChatUser;
    }

    @Test
    @Transactional
    void createChatUserWithExistingId() throws Exception {
        // Create the ChatUser with an existing ID
        chatUser.setId(1L);
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatUser.setUserId(null);

        // Create the ChatUser, which fails.
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        restChatUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatUser.setUserName(null);

        // Create the ChatUser, which fails.
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        restChatUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatUsers() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.saveAndFlush(chatUser);

        // Get all the chatUserList
        restChatUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)));
    }

    @Test
    @Transactional
    void getChatUser() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.saveAndFlush(chatUser);

        // Get the chatUser
        restChatUserMockMvc
            .perform(get(ENTITY_API_URL_ID, chatUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatUser.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME));
    }

    @Test
    @Transactional
    void getNonExistingChatUser() throws Exception {
        // Get the chatUser
        restChatUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatUser() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.saveAndFlush(chatUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatUser
        ChatUser updatedChatUser = chatUserRepository.findById(chatUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChatUser are not directly saved in db
        em.detach(updatedChatUser);
        updatedChatUser.userId(UPDATED_USER_ID).userName(UPDATED_USER_NAME);
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(updatedChatUser);

        restChatUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatUserToMatchAllProperties(updatedChatUser);
    }

    @Test
    @Transactional
    void putNonExistingChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatUser.setId(longCount.incrementAndGet());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatUser.setId(longCount.incrementAndGet());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatUser.setId(longCount.incrementAndGet());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatUserWithPatch() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.saveAndFlush(chatUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatUser using partial update
        ChatUser partialUpdatedChatUser = new ChatUser();
        partialUpdatedChatUser.setId(chatUser.getId());

        partialUpdatedChatUser.userId(UPDATED_USER_ID);

        restChatUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatUser))
            )
            .andExpect(status().isOk());

        // Validate the ChatUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedChatUser, chatUser), getPersistedChatUser(chatUser));
    }

    @Test
    @Transactional
    void fullUpdateChatUserWithPatch() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.saveAndFlush(chatUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatUser using partial update
        ChatUser partialUpdatedChatUser = new ChatUser();
        partialUpdatedChatUser.setId(chatUser.getId());

        partialUpdatedChatUser.userId(UPDATED_USER_ID).userName(UPDATED_USER_NAME);

        restChatUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatUser))
            )
            .andExpect(status().isOk());

        // Validate the ChatUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatUserUpdatableFieldsEquals(partialUpdatedChatUser, getPersistedChatUser(partialUpdatedChatUser));
    }

    @Test
    @Transactional
    void patchNonExistingChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatUser.setId(longCount.incrementAndGet());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatUser.setId(longCount.incrementAndGet());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatUser.setId(longCount.incrementAndGet());

        // Create the ChatUser
        ChatUserDTO chatUserDTO = chatUserMapper.toDto(chatUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chatUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatUser() throws Exception {
        // Initialize the database
        insertedChatUser = chatUserRepository.saveAndFlush(chatUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chatUser
        restChatUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chatUserRepository.count();
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

    protected ChatUser getPersistedChatUser(ChatUser chatUser) {
        return chatUserRepository.findById(chatUser.getId()).orElseThrow();
    }

    protected void assertPersistedChatUserToMatchAllProperties(ChatUser expectedChatUser) {
        assertChatUserAllPropertiesEquals(expectedChatUser, getPersistedChatUser(expectedChatUser));
    }

    protected void assertPersistedChatUserToMatchUpdatableProperties(ChatUser expectedChatUser) {
        assertChatUserAllUpdatablePropertiesEquals(expectedChatUser, getPersistedChatUser(expectedChatUser));
    }
}
