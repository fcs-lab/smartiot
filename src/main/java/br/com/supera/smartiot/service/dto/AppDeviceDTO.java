package br.com.supera.smartiot.service.dto;

import br.com.supera.smartiot.domain.enumeration.DeviceType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.AppDevice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppDeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private String deviceId;

    @NotNull
    private DeviceType deviceType;

    private VehicleInfoDTO vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
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
        if (!(o instanceof AppDeviceDTO)) {
            return false;
        }

        AppDeviceDTO appDeviceDTO = (AppDeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appDeviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppDeviceDTO{" +
            "id=" + getId() +
            ", deviceId='" + getDeviceId() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", vehicle=" + getVehicle() +
            "}";
    }
}
