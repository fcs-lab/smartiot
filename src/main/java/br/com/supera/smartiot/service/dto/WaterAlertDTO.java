package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.WaterAlert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaterAlertDTO implements Serializable {

    private Long id;

    @NotNull
    private String alertType;

    @NotNull
    private String alertDescription;

    @NotNull
    private ZonedDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public void setAlertDescription(String alertDescription) {
        this.alertDescription = alertDescription;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaterAlertDTO)) {
            return false;
        }

        WaterAlertDTO waterAlertDTO = (WaterAlertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, waterAlertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaterAlertDTO{" +
            "id=" + getId() +
            ", alertType='" + getAlertType() + "'" +
            ", alertDescription='" + getAlertDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
