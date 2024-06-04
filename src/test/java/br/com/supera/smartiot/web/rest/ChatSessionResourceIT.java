package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ChatSessionAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.ChatSession;
import br.com.supera.smartiot.repository.ChatSessionRepository;
import br.com.supera.smartiot.service.dto.ChatSessionDTO;
import br.com.supera.smartiot.service.mapper.ChatSessionMapper;
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
 * Integration tests for the {@link ChatSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatSessionResourceIT {

    private static final String DEFAULT_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/chat-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatSessionMockMvc;

    private ChatSession chatSession;

    private ChatSession insertedChatSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatSession createEntity(EntityManager em) {
        ChatSession chatSession = new ChatSession().sessionId(DEFAULT_SESSION_ID).startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME);
        return chatSession;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatSession createUpdatedEntity(EntityManager em) {
        ChatSession chatSession = new ChatSession().sessionId(UPDATED_SESSION_ID).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        return chatSession;
    }

    @BeforeEach
    public void initTest() {
        chatSession = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedChatSession != null) {
            chatSessionRepository.delete(insertedChatSession);
            insertedChatSession = null;
        }
    }

    @Test
    @Transactional
    void createChatSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);
        var returnedChatSessionDTO = om.readValue(
            restChatSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatSessionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChatSessionDTO.class
        );

        // Validate the ChatSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChatSession = chatSessionMapper.toEntity(returnedChatSessionDTO);
        assertChatSessionUpdatableFieldsEquals(returnedChatSession, getPersistedChatSession(returnedChatSession));

        insertedChatSession = returnedChatSession;
    }

    @Test
    @Transactional
    void createChatSessionWithExistingId() throws Exception {
        // Create the ChatSession with an existing ID
        chatSession.setId(1L);
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSessionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatSession.setSessionId(null);

        // Create the ChatSession, which fails.
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        restChatSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatSession.setStartTime(null);

        // Create the ChatSession, which fails.
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        restChatSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatSessions() throws Exception {
        // Initialize the database
        insertedChatSession = chatSessionRepository.saveAndFlush(chatSession);

        // Get all the chatSessionList
        restChatSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    void getChatSession() throws Exception {
        // Initialize the database
        insertedChatSession = chatSessionRepository.saveAndFlush(chatSession);

        // Get the chatSession
        restChatSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, chatSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatSession.getId().intValue()))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChatSession() throws Exception {
        // Get the chatSession
        restChatSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatSession() throws Exception {
        // Initialize the database
        insertedChatSession = chatSessionRepository.saveAndFlush(chatSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatSession
        ChatSession updatedChatSession = chatSessionRepository.findById(chatSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChatSession are not directly saved in db
        em.detach(updatedChatSession);
        updatedChatSession.sessionId(UPDATED_SESSION_ID).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(updatedChatSession);

        restChatSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatSessionToMatchAllProperties(updatedChatSession);
    }

    @Test
    @Transactional
    void putNonExistingChatSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatSession.setId(longCount.incrementAndGet());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatSession.setId(longCount.incrementAndGet());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatSession.setId(longCount.incrementAndGet());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatSessionWithPatch() throws Exception {
        // Initialize the database
        insertedChatSession = chatSessionRepository.saveAndFlush(chatSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatSession using partial update
        ChatSession partialUpdatedChatSession = new ChatSession();
        partialUpdatedChatSession.setId(chatSession.getId());

        partialUpdatedChatSession.endTime(UPDATED_END_TIME);

        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatSession))
            )
            .andExpect(status().isOk());

        // Validate the ChatSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChatSession, chatSession),
            getPersistedChatSession(chatSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateChatSessionWithPatch() throws Exception {
        // Initialize the database
        insertedChatSession = chatSessionRepository.saveAndFlush(chatSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatSession using partial update
        ChatSession partialUpdatedChatSession = new ChatSession();
        partialUpdatedChatSession.setId(chatSession.getId());

        partialUpdatedChatSession.sessionId(UPDATED_SESSION_ID).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatSession))
            )
            .andExpect(status().isOk());

        // Validate the ChatSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatSessionUpdatableFieldsEquals(partialUpdatedChatSession, getPersistedChatSession(partialUpdatedChatSession));
    }

    @Test
    @Transactional
    void patchNonExistingChatSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatSession.setId(longCount.incrementAndGet());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatSessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatSession.setId(longCount.incrementAndGet());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatSession.setId(longCount.incrementAndGet());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chatSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatSession() throws Exception {
        // Initialize the database
        insertedChatSession = chatSessionRepository.saveAndFlush(chatSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chatSession
        restChatSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chatSessionRepository.count();
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

    protected ChatSession getPersistedChatSession(ChatSession chatSession) {
        return chatSessionRepository.findById(chatSession.getId()).orElseThrow();
    }

    protected void assertPersistedChatSessionToMatchAllProperties(ChatSession expectedChatSession) {
        assertChatSessionAllPropertiesEquals(expectedChatSession, getPersistedChatSession(expectedChatSession));
    }

    protected void assertPersistedChatSessionToMatchUpdatableProperties(ChatSession expectedChatSession) {
        assertChatSessionAllUpdatablePropertiesEquals(expectedChatSession, getPersistedChatSession(expectedChatSession));
    }
}
