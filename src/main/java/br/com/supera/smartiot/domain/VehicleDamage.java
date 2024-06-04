package br.com.supera.smartiot.domain;

import br.com.supera.smartiot.domain.enumeration.DamageStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleDamage.
 */
@Entity
@Table(name = "vehicle_damage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleDamage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "damage_description", nullable = false)
    private String damageDescription;

    @NotNull
    @Column(name = "reported_at", nullable = false)
    private Instant reportedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "damage_status", nullable = false)
    private DamageStatus damageStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "damages", "reservations", "services", "alerts", "devices" }, allowSetters = true)
    private VehicleInfo vehicle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleDamage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDamageDescription() {
        return this.damageDescription;
    }

    public VehicleDamage damageDescription(String damageDescription) {
        this.setDamageDescription(damageDescription);
        return this;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public Instant getReportedAt() {
        return this.reportedAt;
    }

    public VehicleDamage reportedAt(Instant reportedAt) {
        this.setReportedAt(reportedAt);
        return this;
    }

    public void setReportedAt(Instant reportedAt) {
        this.reportedAt = reportedAt;
    }

    public DamageStatus getDamageStatus() {
        return this.damageStatus;
    }

    public VehicleDamage damageStatus(DamageStatus damageStatus) {
        this.setDamageStatus(damageStatus);
        return this;
    }

    public void setDamageStatus(DamageStatus damageStatus) {
        this.damageStatus = damageStatus;
    }

    public VehicleInfo getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(VehicleInfo vehicleInfo) {
        this.vehicle = vehicleInfo;
    }

    public VehicleDamage vehicle(VehicleInfo vehicleInfo) {
        this.setVehicle(vehicleInfo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleDamage)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleDamage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleDamage{" +
            "id=" + getId() +
            ", damageDescription='" + getDamageDescription() + "'" +
            ", reportedAt='" + getReportedAt() + "'" +
            ", damageStatus='" + getDamageStatus() + "'" +
            "}";
    }
}
