package br.com.supera.smartiot.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.UserReport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserReportDTO implements Serializable {

    private Long id;

    @NotNull
    private String reportType;

    @NotNull
    private Instant generatedAt;

    @Lob
    private String reportContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserReportDTO)) {
            return false;
        }

        UserReportDTO userReportDTO = (UserReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserReportDTO{" +
            "id=" + getId() +
            ", reportType='" + getReportType() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", reportContent='" + getReportContent() + "'" +
            "}";
    }
}
