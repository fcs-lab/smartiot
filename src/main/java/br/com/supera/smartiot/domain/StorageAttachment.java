package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StorageAttachment.
 */
@Entity
@Table(name = "storage_attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StorageAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "attachment_name", nullable = false)
    private String attachmentName;

    @NotNull
    @Column(name = "record_type", nullable = false)
    private String recordType;

    @NotNull
    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "blob_id", nullable = false)
    private Long blobId;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StorageAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentName() {
        return this.attachmentName;
    }

    public StorageAttachment attachmentName(String attachmentName) {
        this.setAttachmentName(attachmentName);
        return this;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getRecordType() {
        return this.recordType;
    }

    public StorageAttachment recordType(String recordType) {
        this.setRecordType(recordType);
        return this;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Long getRecordId() {
        return this.recordId;
    }

    public StorageAttachment recordId(Long recordId) {
        this.setRecordId(recordId);
        return this;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public StorageAttachment createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public StorageAttachment updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public StorageAttachment lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getBlobId() {
        return this.blobId;
    }

    public StorageAttachment blobId(Long blobId) {
        this.setBlobId(blobId);
        return this;
    }

    public void setBlobId(Long blobId) {
        this.blobId = blobId;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public StorageAttachment deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageAttachment)) {
            return false;
        }
        return getId() != null && getId().equals(((StorageAttachment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageAttachment{" +
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
