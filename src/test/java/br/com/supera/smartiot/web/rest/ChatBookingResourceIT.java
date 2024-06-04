package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ChatBookingAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.ChatBooking;
import br.com.supera.smartiot.repository.ChatBookingRepository;
import br.com.supera.smartiot.service.dto.ChatBookingDTO;
import br.com.supera.smartiot.service.mapper.ChatBookingMapper;
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
 * Integration tests for the {@link ChatBookingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatBookingResourceIT {

    private static final Instant DEFAULT_BOOKING_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BOOKING_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/chat-bookings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatBookingRepository chatBookingRepository;

    @Autowired
    private ChatBookingMapper chatBookingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatBookingMockMvc;

    private ChatBooking chatBooking;

    private ChatBooking insertedChatBooking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatBooking createEntity(EntityManager em) {
        ChatBooking chatBooking = new ChatBooking().bookingTimestamp(DEFAULT_BOOKING_TIMESTAMP);
        return chatBooking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatBooking createUpdatedEntity(EntityManager em) {
        ChatBooking chatBooking = new ChatBooking().bookingTimestamp(UPDATED_BOOKING_TIMESTAMP);
        return chatBooking;
    }

    @BeforeEach
    public void initTest() {
        chatBooking = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedChatBooking != null) {
            chatBookingRepository.delete(insertedChatBooking);
            insertedChatBooking = null;
        }
    }

    @Test
    @Transactional
    void createChatBooking() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChatBooking
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);
        var returnedChatBookingDTO = om.readValue(
            restChatBookingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatBookingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChatBookingDTO.class
        );

        // Validate the ChatBooking in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChatBooking = chatBookingMapper.toEntity(returnedChatBookingDTO);
        assertChatBookingUpdatableFieldsEquals(returnedChatBooking, getPersistedChatBooking(returnedChatBooking));

        insertedChatBooking = returnedChatBooking;
    }

    @Test
    @Transactional
    void createChatBookingWithExistingId() throws Exception {
        // Create the ChatBooking with an existing ID
        chatBooking.setId(1L);
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatBookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBookingTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatBooking.setBookingTimestamp(null);

        // Create the ChatBooking, which fails.
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);

        restChatBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatBookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatBookings() throws Exception {
        // Initialize the database
        insertedChatBooking = chatBookingRepository.saveAndFlush(chatBooking);

        // Get all the chatBookingList
        restChatBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatBooking.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingTimestamp").value(hasItem(DEFAULT_BOOKING_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getChatBooking() throws Exception {
        // Initialize the database
        insertedChatBooking = chatBookingRepository.saveAndFlush(chatBooking);

        // Get the chatBooking
        restChatBookingMockMvc
            .perform(get(ENTITY_API_URL_ID, chatBooking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatBooking.getId().intValue()))
            .andExpect(jsonPath("$.bookingTimestamp").value(DEFAULT_BOOKING_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChatBooking() throws Exception {
        // Get the chatBooking
        restChatBookingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatBooking() throws Exception {
        // Initialize the database
        insertedChatBooking = chatBookingRepository.saveAndFlush(chatBooking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatBooking
        ChatBooking updatedChatBooking = chatBookingRepository.findById(chatBooking.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChatBooking are not directly saved in db
        em.detach(updatedChatBooking);
        updatedChatBooking.bookingTimestamp(UPDATED_BOOKING_TIMESTAMP);
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(updatedChatBooking);

        restChatBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatBookingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatBookingDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatBookingToMatchAllProperties(updatedChatBooking);
    }

    @Test
    @Transactional
    void putNonExistingChatBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatBooking.setId(longCount.incrementAndGet());

        // Create the ChatBooking
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatBookingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatBookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatBooking.setId(longCount.incrementAndGet());

        // Create the ChatBooking
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatBookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatBooking.setId(longCount.incrementAndGet());

        // Create the ChatBooking
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatBookingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatBookingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatBookingWithPatch() throws Exception {
        // Initialize the database
        insertedChatBooking = chatBookingRepository.saveAndFlush(chatBooking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatBooking using partial update
        ChatBooking partialUpdatedChatBooking = new ChatBooking();
        partialUpdatedChatBooking.setId(chatBooking.getId());

        partialUpdatedChatBooking.bookingTimestamp(UPDATED_BOOKING_TIMESTAMP);

        restChatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatBooking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatBooking))
            )
            .andExpect(status().isOk());

        // Validate the ChatBooking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatBookingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChatBooking, chatBooking),
            getPersistedChatBooking(chatBooking)
        );
    }

    @Test
    @Transactional
    void fullUpdateChatBookingWithPatch() throws Exception {
        // Initialize the database
        insertedChatBooking = chatBookingRepository.saveAndFlush(chatBooking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatBooking using partial update
        ChatBooking partialUpdatedChatBooking = new ChatBooking();
        partialUpdatedChatBooking.setId(chatBooking.getId());

        partialUpdatedChatBooking.bookingTimestamp(UPDATED_BOOKING_TIMESTAMP);

        restChatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatBooking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatBooking))
            )
            .andExpect(status().isOk());

        // Validate the ChatBooking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatBookingUpdatableFieldsEquals(partialUpdatedChatBooking, getPersistedChatBooking(partialUpdatedChatBooking));
    }

    @Test
    @Transactional
    void patchNonExistingChatBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatBooking.setId(longCount.incrementAndGet());

        // Create the ChatBooking
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatBookingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatBookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatBooking.setId(longCount.incrementAndGet());

        // Create the ChatBooking
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatBookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatBooking.setId(longCount.incrementAndGet());

        // Create the ChatBooking
        ChatBookingDTO chatBookingDTO = chatBookingMapper.toDto(chatBooking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatBookingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chatBookingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatBooking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatBooking() throws Exception {
        // Initialize the database
        insertedChatBooking = chatBookingRepository.saveAndFlush(chatBooking);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chatBooking
        restChatBookingMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatBooking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chatBookingRepository.count();
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

    protected ChatBooking getPersistedChatBooking(ChatBooking chatBooking) {
        return chatBookingRepository.findById(chatBooking.getId()).orElseThrow();
    }

    protected void assertPersistedChatBookingToMatchAllProperties(ChatBooking expectedChatBooking) {
        assertChatBookingAllPropertiesEquals(expectedChatBooking, getPersistedChatBooking(expectedChatBooking));
    }

    protected void assertPersistedChatBookingToMatchUpdatableProperties(ChatBooking expectedChatBooking) {
        assertChatBookingAllUpdatablePropertiesEquals(expectedChatBooking, getPersistedChatBooking(expectedChatBooking));
    }
}
