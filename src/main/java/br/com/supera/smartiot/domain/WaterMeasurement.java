package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WaterMeasurement.
 */
@Entity
@Table(name = "water_measurement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaterMeasurement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "measurement_date", nullable = false)
    private Instant measurementDate;

    @NotNull
    @Column(name = "water_level", nullable = false)
    private Float waterLevel;

    @Column(name = "water_quality")
    private String waterQuality;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WaterMeasurement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMeasurementDate() {
        return this.measurementDate;
    }

    public WaterMeasurement measurementDate(Instant measurementDate) {
        this.setMeasurementDate(measurementDate);
        return this;
    }

    public void setMeasurementDate(Instant measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Float getWaterLevel() {
        return this.waterLevel;
    }

    public WaterMeasurement waterLevel(Float waterLevel) {
        this.setWaterLevel(waterLevel);
        return this;
    }

    public void setWaterLevel(Float waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getWaterQuality() {
        return this.waterQuality;
    }

    public WaterMeasurement waterQuality(String waterQuality) {
        this.setWaterQuality(waterQuality);
        return this;
    }

    public void setWaterQuality(String waterQuality) {
        this.waterQuality = waterQuality;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaterMeasurement)) {
            return false;
        }
        return getId() != null && getId().equals(((WaterMeasurement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaterMeasurement{" +
            "id=" + getId() +
            ", measurementDate='" + getMeasurementDate() + "'" +
            ", waterLevel=" + getWaterLevel() +
            ", waterQuality='" + getWaterQuality() + "'" +
            "}";
    }
}
