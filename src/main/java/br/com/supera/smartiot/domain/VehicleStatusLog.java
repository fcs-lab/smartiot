package br.com.supera.smartiot.domain;

import br.com.supera.smartiot.domain.enumeration.VehicleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleStatusLog.
 */
@Entity
@Table(name = "vehicle_status_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleStatusLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "status_change_date", nullable = false)
    private Instant statusChangeDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private VehicleStatus newStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleStatusLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStatusChangeDate() {
        return this.statusChangeDate;
    }

    public VehicleStatusLog statusChangeDate(Instant statusChangeDate) {
        this.setStatusChangeDate(statusChangeDate);
        return this;
    }

    public void setStatusChangeDate(Instant statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }

    public VehicleStatus getNewStatus() {
        return this.newStatus;
    }

    public VehicleStatusLog newStatus(VehicleStatus newStatus) {
        this.setNewStatus(newStatus);
        return this;
    }

    public void setNewStatus(VehicleStatus newStatus) {
        this.newStatus = newStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleStatusLog)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleStatusLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleStatusLog{" +
            "id=" + getId() +
            ", statusChangeDate='" + getStatusChangeDate() + "'" +
            ", newStatus='" + getNewStatus() + "'" +
            "}";
    }
}
