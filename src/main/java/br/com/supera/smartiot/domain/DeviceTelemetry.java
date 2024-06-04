package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeviceTelemetry.
 */
@Entity
@Table(name = "device_telemetry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceTelemetry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "telemetry_timestamp", nullable = false)
    private Instant telemetryTimestamp;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "speed")
    private Float speed;

    @Column(name = "fuel_level")
    private Float fuelLevel;

    @Column(name = "engine_status")
    private String engineStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeviceTelemetry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTelemetryTimestamp() {
        return this.telemetryTimestamp;
    }

    public DeviceTelemetry telemetryTimestamp(Instant telemetryTimestamp) {
        this.setTelemetryTimestamp(telemetryTimestamp);
        return this;
    }

    public void setTelemetryTimestamp(Instant telemetryTimestamp) {
        this.telemetryTimestamp = telemetryTimestamp;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public DeviceTelemetry latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public DeviceTelemetry longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getSpeed() {
        return this.speed;
    }

    public DeviceTelemetry speed(Float speed) {
        this.setSpeed(speed);
        return this;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getFuelLevel() {
        return this.fuelLevel;
    }

    public DeviceTelemetry fuelLevel(Float fuelLevel) {
        this.setFuelLevel(fuelLevel);
        return this;
    }

    public void setFuelLevel(Float fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public String getEngineStatus() {
        return this.engineStatus;
    }

    public DeviceTelemetry engineStatus(String engineStatus) {
        this.setEngineStatus(engineStatus);
        return this;
    }

    public void setEngineStatus(String engineStatus) {
        this.engineStatus = engineStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceTelemetry)) {
            return false;
        }
        return getId() != null && getId().equals(((DeviceTelemetry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceTelemetry{" +
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
