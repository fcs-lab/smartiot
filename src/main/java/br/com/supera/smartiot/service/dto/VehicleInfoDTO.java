package br.com.supera.smartiot.service.dto;

import br.com.supera.smartiot.domain.enumeration.VehicleStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.VehicleInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleInfoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String modelName;

    @NotNull
    @Size(max = 10)
    private String licensePlate;

    @NotNull
    private VehicleStatus vehicleStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleInfoDTO)) {
            return false;
        }

        VehicleInfoDTO vehicleInfoDTO = (VehicleInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleInfoDTO{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            ", licensePlate='" + getLicensePlate() + "'" +
            ", vehicleStatus='" + getVehicleStatus() + "'" +
            "}";
    }
}
