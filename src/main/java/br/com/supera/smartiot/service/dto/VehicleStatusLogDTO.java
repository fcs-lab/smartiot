package br.com.supera.smartiot.service.dto;

import br.com.supera.smartiot.domain.enumeration.VehicleStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.VehicleStatusLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleStatusLogDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant statusChangeDate;

    @NotNull
    private VehicleStatus newStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(Instant statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }

    public VehicleStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(VehicleStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleStatusLogDTO)) {
            return false;
        }

        VehicleStatusLogDTO vehicleStatusLogDTO = (VehicleStatusLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleStatusLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleStatusLogDTO{" +
            "id=" + getId() +
            ", statusChangeDate='" + getStatusChangeDate() + "'" +
            ", newStatus='" + getNewStatus() + "'" +
            "}";
    }
}
