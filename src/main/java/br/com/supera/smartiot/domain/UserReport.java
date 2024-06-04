package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserReport.
 */
@Entity
@Table(name = "user_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "report_type", nullable = false)
    private String reportType;

    @NotNull
    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt;

    @Lob
    @Column(name = "report_content", nullable = false)
    private String reportContent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportType() {
        return this.reportType;
    }

    public UserReport reportType(String reportType) {
        this.setReportType(reportType);
        return this;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Instant getGeneratedAt() {
        return this.generatedAt;
    }

    public UserReport generatedAt(Instant generatedAt) {
        this.setGeneratedAt(generatedAt);
        return this;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getReportContent() {
        return this.reportContent;
    }

    public UserReport reportContent(String reportContent) {
        this.setReportContent(reportContent);
        return this;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserReport)) {
            return false;
        }
        return getId() != null && getId().equals(((UserReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserReport{" +
            "id=" + getId() +
            ", reportType='" + getReportType() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", reportContent='" + getReportContent() + "'" +
            "}";
    }
}
