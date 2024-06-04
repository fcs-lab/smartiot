package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.DeviceTelemetry} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceTelemetryDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant telemetryTimestamp;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private Float speed;

    private Float fuelLevel;

    private String engineStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTelemetryTimestamp() {
        return telemetryTimestamp;
    }

    public void setTelemetryTimestamp(Instant telemetryTimestamp) {
        this.telemetryTimestamp = telemetryTimestamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(Float fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public String getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(String engineStatus) {
        this.engineStatus = engineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceTelemetryDTO)) {
            return false;
        }

        DeviceTelemetryDTO deviceTelemetryDTO = (DeviceTelemetryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceTelemetryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceTelemetryDTO{" +
            "id=" + getId() +
            ", telemetryTimestamp='" + getTelemetryTimestamp() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", speed=" + getSpeed() +
            ", fuelLevel=" + getFuelLevel() +
            ", engineStatus='" + getEngineStatus() + "'" +
            "}";
    }
}
