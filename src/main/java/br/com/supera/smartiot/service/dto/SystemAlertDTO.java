package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.SystemAlert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemAlertDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private String alertDescription;

    @NotNull
    private String alertType;

    private VehicleInfoDTO vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAlertDescription() {
        return alertDescription;
    }

    public void setAlertDescription(String alertDescription) {
        this.alertDescription = alertDescription;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public VehicleInfoDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleInfoDTO vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemAlertDTO)) {
            return false;
        }

        SystemAlertDTO systemAlertDTO = (SystemAlertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemAlertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemAlertDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", alertDescription='" + getAlertDescription() + "'" +
            ", alertType='" + getAlertType() + "'" +
            ", vehicle=" + getVehicle() +
            "}";
    }
}
