package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.StorageBlobAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.StorageBlob;
import br.com.supera.smartiot.repository.StorageBlobRepository;
import br.com.supera.smartiot.service.dto.StorageBlobDTO;
import br.com.supera.smartiot.service.mapper.StorageBlobMapper;
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
 * Integration tests for the {@link StorageBlobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StorageBlobResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_BYTE_SIZE = 1L;
    private static final Long UPDATED_BYTE_SIZE = 2L;

    private static final String DEFAULT_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_CHECKSUM = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/storage-blobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StorageBlobRepository storageBlobRepository;

    @Autowired
    private StorageBlobMapper storageBlobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageBlobMockMvc;

    private StorageBlob storageBlob;

    private StorageBlob insertedStorageBlob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageBlob createEntity(EntityManager em) {
        StorageBlob storageBlob = new StorageBlob()
            .fileName(DEFAULT_FILE_NAME)
            .contentType(DEFAULT_CONTENT_TYPE)
            .byteSize(DEFAULT_BYTE_SIZE)
            .checksum(DEFAULT_CHECKSUM)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .key(DEFAULT_KEY)
            .metadata(DEFAULT_METADATA)
            .deletedAt(DEFAULT_DELETED_AT);
        return storageBlob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageBlob createUpdatedEntity(EntityManager em) {
        StorageBlob storageBlob = new StorageBlob()
            .fileName(UPDATED_FILE_NAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .byteSize(UPDATED_BYTE_SIZE)
            .checksum(UPDATED_CHECKSUM)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .key(UPDATED_KEY)
            .metadata(UPDATED_METADATA)
            .deletedAt(UPDATED_DELETED_AT);
        return storageBlob;
    }

    @BeforeEach
    public void initTest() {
        storageBlob = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedStorageBlob != null) {
            storageBlobRepository.delete(insertedStorageBlob);
            insertedStorageBlob = null;
        }
    }

    @Test
    @Transactional
    void createStorageBlob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StorageBlob
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);
        var returnedStorageBlobDTO = om.readValue(
            restStorageBlobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StorageBlobDTO.class
        );

        // Validate the StorageBlob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStorageBlob = storageBlobMapper.toEntity(returnedStorageBlobDTO);
        assertStorageBlobUpdatableFieldsEquals(returnedStorageBlob, getPersistedStorageBlob(returnedStorageBlob));

        insertedStorageBlob = returnedStorageBlob;
    }

    @Test
    @Transactional
    void createStorageBlobWithExistingId() throws Exception {
        // Create the StorageBlob with an existing ID
        storageBlob.setId(1L);
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageBlob.setFileName(null);

        // Create the StorageBlob, which fails.
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkByteSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageBlob.setByteSize(null);

        // Create the StorageBlob, which fails.
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChecksumIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageBlob.setChecksum(null);

        // Create the StorageBlob, which fails.
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageBlob.setCreatedAt(null);

        // Create the StorageBlob, which fails.
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageBlob.setUpdatedAt(null);

        // Create the StorageBlob, which fails.
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        storageBlob.setKey(null);

        // Create the StorageBlob, which fails.
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        restStorageBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStorageBlobs() throws Exception {
        // Initialize the database
        insertedStorageBlob = storageBlobRepository.saveAndFlush(storageBlob);

        // Get all the storageBlobList
        restStorageBlobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageBlob.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].byteSize").value(hasItem(DEFAULT_BYTE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }

    @Test
    @Transactional
    void getStorageBlob() throws Exception {
        // Initialize the database
        insertedStorageBlob = storageBlobRepository.saveAndFlush(storageBlob);

        // Get the storageBlob
        restStorageBlobMockMvc
            .perform(get(ENTITY_API_URL_ID, storageBlob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storageBlob.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.byteSize").value(DEFAULT_BYTE_SIZE.intValue()))
            .andExpect(jsonPath("$.checksum").value(DEFAULT_CHECKSUM))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStorageBlob() throws Exception {
        // Get the storageBlob
        restStorageBlobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStorageBlob() throws Exception {
        // Initialize the database
        insertedStorageBlob = storageBlobRepository.saveAndFlush(storageBlob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageBlob
        StorageBlob updatedStorageBlob = storageBlobRepository.findById(storageBlob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStorageBlob are not directly saved in db
        em.detach(updatedStorageBlob);
        updatedStorageBlob
            .fileName(UPDATED_FILE_NAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .byteSize(UPDATED_BYTE_SIZE)
            .checksum(UPDATED_CHECKSUM)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .key(UPDATED_KEY)
            .metadata(UPDATED_METADATA)
            .deletedAt(UPDATED_DELETED_AT);
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(updatedStorageBlob);

        restStorageBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageBlobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageBlobDTO))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStorageBlobToMatchAllProperties(updatedStorageBlob);
    }

    @Test
    @Transactional
    void putNonExistingStorageBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageBlob.setId(longCount.incrementAndGet());

        // Create the StorageBlob
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageBlobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageBlobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorageBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageBlob.setId(longCount.incrementAndGet());

        // Create the StorageBlob
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageBlobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorageBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageBlob.setId(longCount.incrementAndGet());

        // Create the StorageBlob
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStorageBlobWithPatch() throws Exception {
        // Initialize the database
        insertedStorageBlob = storageBlobRepository.saveAndFlush(storageBlob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageBlob using partial update
        StorageBlob partialUpdatedStorageBlob = new StorageBlob();
        partialUpdatedStorageBlob.setId(storageBlob.getId());

        partialUpdatedStorageBlob
            .fileName(UPDATED_FILE_NAME)
            .checksum(UPDATED_CHECKSUM)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .key(UPDATED_KEY);

        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageBlob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorageBlob))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageBlobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStorageBlob, storageBlob),
            getPersistedStorageBlob(storageBlob)
        );
    }

    @Test
    @Transactional
    void fullUpdateStorageBlobWithPatch() throws Exception {
        // Initialize the database
        insertedStorageBlob = storageBlobRepository.saveAndFlush(storageBlob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageBlob using partial update
        StorageBlob partialUpdatedStorageBlob = new StorageBlob();
        partialUpdatedStorageBlob.setId(storageBlob.getId());

        partialUpdatedStorageBlob
            .fileName(UPDATED_FILE_NAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .byteSize(UPDATED_BYTE_SIZE)
            .checksum(UPDATED_CHECKSUM)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .key(UPDATED_KEY)
            .metadata(UPDATED_METADATA)
            .deletedAt(UPDATED_DELETED_AT);

        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageBlob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorageBlob))
            )
            .andExpect(status().isOk());

        // Validate the StorageBlob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageBlobUpdatableFieldsEquals(partialUpdatedStorageBlob, getPersistedStorageBlob(partialUpdatedStorageBlob));
    }

    @Test
    @Transactional
    void patchNonExistingStorageBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageBlob.setId(longCount.incrementAndGet());

        // Create the StorageBlob
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageBlobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageBlobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorageBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageBlob.setId(longCount.incrementAndGet());

        // Create the StorageBlob
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageBlobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorageBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageBlob.setId(longCount.incrementAndGet());

        // Create the StorageBlob
        StorageBlobDTO storageBlobDTO = storageBlobMapper.toDto(storageBlob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageBlobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(storageBlobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStorageBlob() throws Exception {
        // Initialize the database
        insertedStorageBlob = storageBlobRepository.saveAndFlush(storageBlob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the storageBlob
        restStorageBlobMockMvc
            .perform(delete(ENTITY_API_URL_ID, storageBlob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return storageBlobRepository.count();
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

    protected StorageBlob getPersistedStorageBlob(StorageBlob storageBlob) {
        return storageBlobRepository.findById(storageBlob.getId()).orElseThrow();
    }

    protected void assertPersistedStorageBlobToMatchAllProperties(StorageBlob expectedStorageBlob) {
        assertStorageBlobAllPropertiesEquals(expectedStorageBlob, getPersistedStorageBlob(expectedStorageBlob));
    }

    protected void assertPersistedStorageBlobToMatchUpdatableProperties(StorageBlob expectedStorageBlob) {
        assertStorageBlobAllUpdatablePropertiesEquals(expectedStorageBlob, getPersistedStorageBlob(expectedStorageBlob));
    }
}
