package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.VehicleService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleServiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String serviceName;

    @NotNull
    private LocalDate serviceDate;

    private String serviceDescription;

    private VehicleInfoDTO vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
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
        if (!(o instanceof VehicleServiceDTO)) {
            return false;
        }

        VehicleServiceDTO vehicleServiceDTO = (VehicleServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleServiceDTO{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", serviceDate='" + getServiceDate() + "'" +
            ", serviceDescription='" + getServiceDescription() + "'" +
            ", vehicle=" + getVehicle() +
            "}";
    }
}
