package br.com.supera.smartiot.domain;

import br.com.supera.smartiot.domain.enumeration.SensorStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WaterSensor.
 */
@Entity
@Table(name = "water_sensor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaterSensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sensor_id", nullable = false)
    private String sensorId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_status", nullable = false)
    private SensorStatus sensorStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WaterSensor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorId() {
        return this.sensorId;
    }

    public WaterSensor sensorId(String sensorId) {
        this.setSensorId(sensorId);
        return this;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public SensorStatus getSensorStatus() {
        return this.sensorStatus;
    }

    public WaterSensor sensorStatus(SensorStatus sensorStatus) {
        this.setSensorStatus(sensorStatus);
        return this;
    }

    public void setSensorStatus(SensorStatus sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaterSensor)) {
            return false;
        }
        return getId() != null && getId().equals(((WaterSensor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaterSensor{" +
            "id=" + getId() +
            ", sensorId='" + getSensorId() + "'" +
            ", sensorStatus='" + getSensorStatus() + "'" +
            "}";
    }
}
