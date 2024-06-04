package br.com.supera.smartiot.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class StorageAttachmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStorageAttachmentAllPropertiesEquals(StorageAttachment expected, StorageAttachment actual) {
        assertStorageAttachmentAutoGeneratedPropertiesEquals(expected, actual);
        assertStorageAttachmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStorageAttachmentAllUpdatablePropertiesEquals(StorageAttachment expected, StorageAttachment actual) {
        assertStorageAttachmentUpdatableFieldsEquals(expected, actual);
        assertStorageAttachmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStorageAttachmentAutoGeneratedPropertiesEquals(StorageAttachment expected, StorageAttachment actual) {
        assertThat(expected)
            .as("Verify StorageAttachment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStorageAttachmentUpdatableFieldsEquals(StorageAttachment expected, StorageAttachment actual) {
        assertThat(expected)
            .as("Verify StorageAttachment relevant properties")
            .satisfies(e -> assertThat(e.getAttachmentName()).as("check attachmentName").isEqualTo(actual.getAttachmentName()))
            .satisfies(e -> assertThat(e.getRecordType()).as("check recordType").isEqualTo(actual.getRecordType()))
            .satisfies(e -> assertThat(e.getRecordId()).as("check recordId").isEqualTo(actual.getRecordId()))
            .satisfies(e -> assertThat(e.getCreatedAt()).as("check createdAt").isEqualTo(actual.getCreatedAt()))
            .satisfies(e -> assertThat(e.getUpdatedAt()).as("check updatedAt").isEqualTo(actual.getUpdatedAt()))
            .satisfies(e -> assertThat(e.getLastModifiedBy()).as("check lastModifiedBy").isEqualTo(actual.getLastModifiedBy()))
            .satisfies(e -> assertThat(e.getBlobId()).as("check blobId").isEqualTo(actual.getBlobId()))
            .satisfies(e -> assertThat(e.getDeletedAt()).as("check deletedAt").isEqualTo(actual.getDeletedAt()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStorageAttachmentUpdatableRelationshipsEquals(StorageAttachment expected, StorageAttachment actual) {}
}
