package br.com.supera.smartiot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleService.
 */
@Entity
@Table(name = "vehicle_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @NotNull
    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    @Column(name = "service_description")
    private String serviceDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "damages", "reservations", "services", "alerts", "devices" }, allowSetters = true)
    private VehicleInfo vehicle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public VehicleService serviceName(String serviceName) {
        this.setServiceName(serviceName);
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public LocalDate getServiceDate() {
        return this.serviceDate;
    }

    public VehicleService serviceDate(LocalDate serviceDate) {
        this.setServiceDate(serviceDate);
        return this;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceDescription() {
        return this.serviceDescription;
    }

    public VehicleService serviceDescription(String serviceDescription) {
        this.setServiceDescription(serviceDescription);
        return this;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public VehicleInfo getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(VehicleInfo vehicleInfo) {
        this.vehicle = vehicleInfo;
    }

    public VehicleService vehicle(VehicleInfo vehicleInfo) {
        this.setVehicle(vehicleInfo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleService)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleService) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleService{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", serviceDate='" + getServiceDate() + "'" +
            ", serviceDescription='" + getServiceDescription() + "'" +
            "}";
    }
}
