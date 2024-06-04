package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.StorageAttachment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StorageAttachmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String attachmentName;

    @NotNull
    private String recordType;

    @NotNull
    private Long recordId;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private String lastModifiedBy;

    @NotNull
    private Long blobId;

    private Instant deletedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getBlobId() {
        return blobId;
    }

    public void setBlobId(Long blobId) {
        this.blobId = blobId;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageAttachmentDTO)) {
            return false;
        }

        StorageAttachmentDTO storageAttachmentDTO = (StorageAttachmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storageAttachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageAttachmentDTO{" +
            "id=" + getId() +
            ", attachmentName='" + getAttachmentName() + "'" +
            ", recordType='" + getRecordType() + "'" +
            ", recordId=" + getRecordId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", blobId=" + getBlobId() +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
