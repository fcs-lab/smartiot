package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.StorageAttachmentAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.StorageAttachment;
import br.com.supera.smartiot.repository.StorageAttachmentRepository;
import br.com.supera.smartiot.service.dto.StorageAttachmentDTO;
import br.com.supera.smartiot.service.mapper.StorageAttachmentMapper;
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
 * Integration tests for the {@link StorageAttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StorageAttachmentResourceIT {

    private static final String DEFAULT_ATTACHMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_RECORD_ID = 1L;
    private static final Long UPDATED_RECORD_ID = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_BLOB_ID = 1L;
    private static final Long UPDATED_BLOB_ID = 2L;

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/storage-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StorageAttachmentRepository storageAttachmentRepository;

    @Autowired
    private StorageAttachmentMapper storageAttachmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageAttachmentMockMvc;

    private StorageAttachment storageAttachment;

    private StorageAttachment insertedStorageAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageAttachment createEntity(EntityManager em) {
        StorageAttachment storageAttachment = new StorageAttachment()
            .attachmentName(DEFAULT_ATTACHMENT_NAME)
            .recordType(DEFAULT_RECORD_TYPE)
            .recordId(DEFAULT_RECORD_ID)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .blobId(DEFAULT_BLOB_ID)
            .deletedAt(DEFAULT_DELETED_AT);
        return storageAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageAttachment createUpdatedEntity(EntityManager em) {
        StorageAttachment storageAttachment = new StorageAttachment()
            .attachmentName(UPDATED_ATTACHMENT_NAME)
            .recordType(UPDATED_RECORD_TYPE)
            .recordId(UPDATED_RECORD_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .blobId(UPDATED_BLOB_ID)
            .deletedAt(UPDATED_DELETED_AT);
        return storageAttachment;
    }

    @BeforeEach
    public void initTest() {
        storageAttachment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedStorageAttachment != null) {
            storageAttachmentRepository.delete(insertedStorageAttachment);
            insertedStorageAttachment = null;
        }
    }

    @Test
    @Transactional
    void createStorageAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StorageAttachment
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);
        var returnedStorageAttachmentDTO = om.readValue(
            restStorageAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StorageAttachmentDTO.class
        );

        // Validate the StorageAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStorageAttachment = storageAttachmentMapper.toEntity(returnedStorageAttachmentDTO);
        assertStorageAttachmentUpdatableFieldsEquals(returnedStorageAttachment, getPersistedStorageAttachment(returnedStorageAttachment));

        insertedStorageAttachment = returnedStorageAttachment;
    }

    @Test
    @Transactional
    void createStorageAttachmentWithExistingId() throws Exception {
        // Create the StorageAttachment with an existing ID
        storageAttachment.setId(1L);
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StorageAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAttachmentNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageAttachment.setAttachmentName(null);

        // Create the StorageAttachment, which fails.
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        restStorageAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecordTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageAttachment.setRecordType(null);

        // Create the StorageAttachment, which fails.
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        restStorageAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecordIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageAttachment.setRecordId(null);

        // Create the StorageAttachment, which fails.
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        restStorageAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageAttachment.setCreatedAt(null);

        // Create the StorageAttachment, which fails.
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        restStorageAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageAttachment.setUpdatedAt(null);

        // Create the StorageAttachment, which fails.
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        restStorageAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBlobIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageAttachment.setBlobId(null);

        // Create the StorageAttachment, which fails.
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        restStorageAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStorageAttachments() throws Exception {
        // Initialize the database
        insertedStorageAttachment = storageAttachmentRepository.saveAndFlush(storageAttachment);

        // Get all the storageAttachmentList
        restStorageAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentName").value(hasItem(DEFAULT_ATTACHMENT_NAME)))
            .andExpect(jsonPath("$.[*].recordType").value(hasItem(DEFAULT_RECORD_TYPE)))
            .andExpect(jsonPath("$.[*].recordId").value(hasItem(DEFAULT_RECORD_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].blobId").value(hasItem(DEFAULT_BLOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }

    @Test
    @Transactional
    void getStorageAttachment() throws Exception {
        // Initialize the database
        insertedStorageAttachment = storageAttachmentRepository.saveAndFlush(storageAttachment);

        // Get the storageAttachment
        restStorageAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, storageAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storageAttachment.getId().intValue()))
            .andExpect(jsonPath("$.attachmentName").value(DEFAULT_ATTACHMENT_NAME))
            .andExpect(jsonPath("$.recordType").value(DEFAULT_RECORD_TYPE))
            .andExpect(jsonPath("$.recordId").value(DEFAULT_RECORD_ID.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.blobId").value(DEFAULT_BLOB_ID.intValue()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStorageAttachment() throws Exception {
        // Get the storageAttachment
        restStorageAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStorageAttachment() throws Exception {
        // Initialize the database
        insertedStorageAttachment = storageAttachmentRepository.saveAndFlush(storageAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageAttachment
        StorageAttachment updatedStorageAttachment = storageAttachmentRepository.findById(storageAttachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStorageAttachment are not directly saved in db
        em.detach(updatedStorageAttachment);
        updatedStorageAttachment
            .attachmentName(UPDATED_ATTACHMENT_NAME)
            .recordType(UPDATED_RECORD_TYPE)
            .recordId(UPDATED_RECORD_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .blobId(UPDATED_BLOB_ID)
            .deletedAt(UPDATED_DELETED_AT);
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(updatedStorageAttachment);

        restStorageAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageAttachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageAttachmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the StorageAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStorageAttachmentToMatchAllProperties(updatedStorageAttachment);
    }

    @Test
    @Transactional
    void putNonExistingStorageAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageAttachment.setId(longCount.incrementAndGet());

        // Create the StorageAttachment
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageAttachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorageAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageAttachment.setId(longCount.incrementAndGet());

        // Create the StorageAttachment
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorageAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageAttachment.setId(longCount.incrementAndGet());

        // Create the StorageAttachment
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStorageAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedStorageAttachment = storageAttachmentRepository.saveAndFlush(storageAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageAttachment using partial update
        StorageAttachment partialUpdatedStorageAttachment = new StorageAttachment();
        partialUpdatedStorageAttachment.setId(storageAttachment.getId());

        partialUpdatedStorageAttachment
            .attachmentName(UPDATED_ATTACHMENT_NAME)
            .recordType(UPDATED_RECORD_TYPE)
            .updatedAt(UPDATED_UPDATED_AT)
            .blobId(UPDATED_BLOB_ID);

        restStorageAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorageAttachment))
            )
            .andExpect(status().isOk());

        // Validate the StorageAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStorageAttachment, storageAttachment),
            getPersistedStorageAttachment(storageAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateStorageAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedStorageAttachment = storageAttachmentRepository.saveAndFlush(storageAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageAttachment using partial update
        StorageAttachment partialUpdatedStorageAttachment = new StorageAttachment();
        partialUpdatedStorageAttachment.setId(storageAttachment.getId());

        partialUpdatedStorageAttachment
            .attachmentName(UPDATED_ATTACHMENT_NAME)
            .recordType(UPDATED_RECORD_TYPE)
            .recordId(UPDATED_RECORD_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .blobId(UPDATED_BLOB_ID)
            .deletedAt(UPDATED_DELETED_AT);

        restStorageAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorageAttachment))
            )
            .andExpect(status().isOk());

        // Validate the StorageAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageAttachmentUpdatableFieldsEquals(
            partialUpdatedStorageAttachment,
            getPersistedStorageAttachment(partialUpdatedStorageAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStorageAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageAttachment.setId(longCount.incrementAndGet());

        // Create the StorageAttachment
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageAttachmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorageAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageAttachment.setId(longCount.incrementAndGet());

        // Create the StorageAttachment
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorageAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageAttachment.setId(longCount.incrementAndGet());

        // Create the StorageAttachment
        StorageAttachmentDTO storageAttachmentDTO = storageAttachmentMapper.toDto(storageAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(storageAttachmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStorageAttachment() throws Exception {
        // Initialize the database
        insertedStorageAttachment = storageAttachmentRepository.saveAndFlush(storageAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the storageAttachment
        restStorageAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, storageAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return storageAttachmentRepository.count();
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

    protected StorageAttachment getPersistedStorageAttachment(StorageAttachment storageAttachment) {
        return storageAttachmentRepository.findById(storageAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedStorageAttachmentToMatchAllProperties(StorageAttachment expectedStorageAttachment) {
        assertStorageAttachmentAllPropertiesEquals(expectedStorageAttachment, getPersistedStorageAttachment(expectedStorageAttachment));
    }

    protected void assertPersistedStorageAttachmentToMatchUpdatableProperties(StorageAttachment expectedStorageAttachment) {
        assertStorageAttachmentAllUpdatablePropertiesEquals(
            expectedStorageAttachment,
            getPersistedStorageAttachment(expectedStorageAttachment)
        );
    }
}
