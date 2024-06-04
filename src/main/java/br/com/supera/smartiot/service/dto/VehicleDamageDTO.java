package br.com.supera.smartiot.service.dto;

import br.com.supera.smartiot.domain.enumeration.DamageStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.VehicleDamage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleDamageDTO implements Serializable {

    private Long id;

    @NotNull
    private String damageDescription;

    @NotNull
    private Instant reportedAt;

    @NotNull
    private DamageStatus damageStatus;

    private VehicleInfoDTO vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public Instant getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(Instant reportedAt) {
        this.reportedAt = reportedAt;
    }

    public DamageStatus getDamageStatus() {
        return damageStatus;
    }

    public void setDamageStatus(DamageStatus damageStatus) {
        this.damageStatus = damageStatus;
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
        if (!(o instanceof VehicleDamageDTO)) {
            return false;
        }

        VehicleDamageDTO vehicleDamageDTO = (VehicleDamageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleDamageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleDamageDTO{" +
            "id=" + getId() +
            ", damageDescription='" + getDamageDescription() + "'" +
            ", reportedAt='" + getReportedAt() + "'" +
            ", damageStatus='" + getDamageStatus() + "'" +
            ", vehicle=" + getVehicle() +
            "}";
    }
}
