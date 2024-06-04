package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ChatMessageAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.ChatMessage;
import br.com.supera.smartiot.repository.ChatMessageRepository;
import br.com.supera.smartiot.service.dto.ChatMessageDTO;
import br.com.supera.smartiot.service.mapper.ChatMessageMapper;
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
 * Integration tests for the {@link ChatMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatMessageResourceIT {

    private static final String DEFAULT_MESSAGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_MESSAGE_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MESSAGE_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/chat-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatMessageMockMvc;

    private ChatMessage chatMessage;

    private ChatMessage insertedChatMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatMessage createEntity(EntityManager em) {
        ChatMessage chatMessage = new ChatMessage()
            .messageId(DEFAULT_MESSAGE_ID)
            .messageContent(DEFAULT_MESSAGE_CONTENT)
            .messageTimestamp(DEFAULT_MESSAGE_TIMESTAMP);
        return chatMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatMessage createUpdatedEntity(EntityManager em) {
        ChatMessage chatMessage = new ChatMessage()
            .messageId(UPDATED_MESSAGE_ID)
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .messageTimestamp(UPDATED_MESSAGE_TIMESTAMP);
        return chatMessage;
    }

    @BeforeEach
    public void initTest() {
        chatMessage = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedChatMessage != null) {
            chatMessageRepository.delete(insertedChatMessage);
            insertedChatMessage = null;
        }
    }

    @Test
    @Transactional
    void createChatMessage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);
        var returnedChatMessageDTO = om.readValue(
            restChatMessageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatMessageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChatMessageDTO.class
        );

        // Validate the ChatMessage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChatMessage = chatMessageMapper.toEntity(returnedChatMessageDTO);
        assertChatMessageUpdatableFieldsEquals(returnedChatMessage, getPersistedChatMessage(returnedChatMessage));

        insertedChatMessage = returnedChatMessage;
    }

    @Test
    @Transactional
    void createChatMessageWithExistingId() throws Exception {
        // Create the ChatMessage with an existing ID
        chatMessage.setId(1L);
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMessageIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatMessage.setMessageId(null);

        // Create the ChatMessage, which fails.
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        restChatMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatMessage.setMessageContent(null);

        // Create the ChatMessage, which fails.
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        restChatMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatMessage.setMessageTimestamp(null);

        // Create the ChatMessage, which fails.
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        restChatMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatMessages() throws Exception {
        // Initialize the database
        insertedChatMessage = chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList
        restChatMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID)))
            .andExpect(jsonPath("$.[*].messageContent").value(hasItem(DEFAULT_MESSAGE_CONTENT)))
            .andExpect(jsonPath("$.[*].messageTimestamp").value(hasItem(DEFAULT_MESSAGE_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getChatMessage() throws Exception {
        // Initialize the database
        insertedChatMessage = chatMessageRepository.saveAndFlush(chatMessage);

        // Get the chatMessage
        restChatMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, chatMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatMessage.getId().intValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID))
            .andExpect(jsonPath("$.messageContent").value(DEFAULT_MESSAGE_CONTENT))
            .andExpect(jsonPath("$.messageTimestamp").value(DEFAULT_MESSAGE_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChatMessage() throws Exception {
        // Get the chatMessage
        restChatMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatMessage() throws Exception {
        // Initialize the database
        insertedChatMessage = chatMessageRepository.saveAndFlush(chatMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatMessage
        ChatMessage updatedChatMessage = chatMessageRepository.findById(chatMessage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChatMessage are not directly saved in db
        em.detach(updatedChatMessage);
        updatedChatMessage
            .messageId(UPDATED_MESSAGE_ID)
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .messageTimestamp(UPDATED_MESSAGE_TIMESTAMP);
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(updatedChatMessage);

        restChatMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatMessageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatMessageToMatchAllProperties(updatedChatMessage);
    }

    @Test
    @Transactional
    void putNonExistingChatMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatMessage.setId(longCount.incrementAndGet());

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatMessage.setId(longCount.incrementAndGet());

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatMessage.setId(longCount.incrementAndGet());

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatMessageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatMessageWithPatch() throws Exception {
        // Initialize the database
        insertedChatMessage = chatMessageRepository.saveAndFlush(chatMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatMessage using partial update
        ChatMessage partialUpdatedChatMessage = new ChatMessage();
        partialUpdatedChatMessage.setId(chatMessage.getId());

        partialUpdatedChatMessage.messageId(UPDATED_MESSAGE_ID).messageTimestamp(UPDATED_MESSAGE_TIMESTAMP);

        restChatMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatMessage))
            )
            .andExpect(status().isOk());

        // Validate the ChatMessage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatMessageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChatMessage, chatMessage),
            getPersistedChatMessage(chatMessage)
        );
    }

    @Test
    @Transactional
    void fullUpdateChatMessageWithPatch() throws Exception {
        // Initialize the database
        insertedChatMessage = chatMessageRepository.saveAndFlush(chatMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatMessage using partial update
        ChatMessage partialUpdatedChatMessage = new ChatMessage();
        partialUpdatedChatMessage.setId(chatMessage.getId());

        partialUpdatedChatMessage
            .messageId(UPDATED_MESSAGE_ID)
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .messageTimestamp(UPDATED_MESSAGE_TIMESTAMP);

        restChatMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatMessage))
            )
            .andExpect(status().isOk());

        // Validate the ChatMessage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatMessageUpdatableFieldsEquals(partialUpdatedChatMessage, getPersistedChatMessage(partialUpdatedChatMessage));
    }

    @Test
    @Transactional
    void patchNonExistingChatMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatMessage.setId(longCount.incrementAndGet());

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatMessageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatMessage.setId(longCount.incrementAndGet());

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatMessage.setId(longCount.incrementAndGet());

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatMessageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chatMessageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatMessage() throws Exception {
        // Initialize the database
        insertedChatMessage = chatMessageRepository.saveAndFlush(chatMessage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chatMessage
        restChatMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chatMessageRepository.count();
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

    protected ChatMessage getPersistedChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.findById(chatMessage.getId()).orElseThrow();
    }

    protected void assertPersistedChatMessageToMatchAllProperties(ChatMessage expectedChatMessage) {
        assertChatMessageAllPropertiesEquals(expectedChatMessage, getPersistedChatMessage(expectedChatMessage));
    }

    protected void assertPersistedChatMessageToMatchUpdatableProperties(ChatMessage expectedChatMessage) {
        assertChatMessageAllUpdatablePropertiesEquals(expectedChatMessage, getPersistedChatMessage(expectedChatMessage));
    }
}
