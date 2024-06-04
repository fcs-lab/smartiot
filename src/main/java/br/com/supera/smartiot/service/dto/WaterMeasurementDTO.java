package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.WaterMeasurement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaterMeasurementDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant measurementDate;

    @NotNull
    private Float waterLevel;

    private String waterQuality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(Instant measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(Float waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getWaterQuality() {
        return waterQuality;
    }

    public void setWaterQuality(String waterQuality) {
        this.waterQuality = waterQuality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaterMeasurementDTO)) {
            return false;
        }

        WaterMeasurementDTO waterMeasurementDTO = (WaterMeasurementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, waterMeasurementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaterMeasurementDTO{" +
            "id=" + getId() +
            ", measurementDate='" + getMeasurementDate() + "'" +
            ", waterLevel=" + getWaterLevel() +
            ", waterQuality='" + getWaterQuality() + "'" +
            "}";
    }
}
