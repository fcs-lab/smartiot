package br.com.supera.smartiot.service.dto;

import br.com.supera.smartiot.domain.enumeration.SensorStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.WaterSensor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaterSensorDTO implements Serializable {

    private Long id;

    @NotNull
    private String sensorId;

    @NotNull
    private SensorStatus sensorStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public SensorStatus getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(SensorStatus sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaterSensorDTO)) {
            return false;
        }

        WaterSensorDTO waterSensorDTO = (WaterSensorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, waterSensorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaterSensorDTO{" +
            "id=" + getId() +
            ", sensorId='" + getSensorId() + "'" +
            ", sensorStatus='" + getSensorStatus() + "'" +
            "}";
    }
}
