package br.com.supera.smartiot.domain;

import br.com.supera.smartiot.domain.enumeration.VehicleStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleInfo.
 */
@Entity
@Table(name = "vehicle_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "model_name", length = 50, nullable = false)
    private String modelName;

    @NotNull
    @Size(max = 10)
    @Column(name = "license_plate", length = 10, nullable = false)
    private String licensePlate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_status", nullable = false)
    private VehicleStatus vehicleStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vehicle" }, allowSetters = true)
    private Set<VehicleDamage> damages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "session", "vehicle" }, allowSetters = true)
    private Set<ChatBooking> reservations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vehicle" }, allowSetters = true)
    private Set<VehicleService> services = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vehicle" }, allowSetters = true)
    private Set<SystemAlert> alerts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vehicle" }, allowSetters = true)
    private Set<AppDevice> devices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return this.modelName;
    }

    public VehicleInfo modelName(String modelName) {
        this.setModelName(modelName);
        return this;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public VehicleInfo licensePlate(String licensePlate) {
        this.setLicensePlate(licensePlate);
        return this;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleStatus getVehicleStatus() {
        return this.vehicleStatus;
    }

    public VehicleInfo vehicleStatus(VehicleStatus vehicleStatus) {
        this.setVehicleStatus(vehicleStatus);
        return this;
    }

    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public Set<VehicleDamage> getDamages() {
        return this.damages;
    }

    public void setDamages(Set<VehicleDamage> vehicleDamages) {
        if (this.damages != null) {
            this.damages.forEach(i -> i.setVehicle(null));
        }
        if (vehicleDamages != null) {
            vehicleDamages.forEach(i -> i.setVehicle(this));
        }
        this.damages = vehicleDamages;
    }

    public VehicleInfo damages(Set<VehicleDamage> vehicleDamages) {
        this.setDamages(vehicleDamages);
        return this;
    }

    public VehicleInfo addDamages(VehicleDamage vehicleDamage) {
        this.damages.add(vehicleDamage);
        vehicleDamage.setVehicle(this);
        return this;
    }

    public VehicleInfo removeDamages(VehicleDamage vehicleDamage) {
        this.damages.remove(vehicleDamage);
        vehicleDamage.setVehicle(null);
        return this;
    }

    public Set<ChatBooking> getReservations() {
        return this.reservations;
    }

    public void setReservations(Set<ChatBooking> chatBookings) {
        if (this.reservations != null) {
            this.reservations.forEach(i -> i.setVehicle(null));
        }
        if (chatBookings != null) {
            chatBookings.forEach(i -> i.setVehicle(this));
        }
        this.reservations = chatBookings;
    }

    public VehicleInfo reservations(Set<ChatBooking> chatBookings) {
        this.setReservations(chatBookings);
        return this;
    }

    public VehicleInfo addReservations(ChatBooking chatBooking) {
        this.reservations.add(chatBooking);
        chatBooking.setVehicle(this);
        return this;
    }

    public VehicleInfo removeReservations(ChatBooking chatBooking) {
        this.reservations.remove(chatBooking);
        chatBooking.setVehicle(null);
        return this;
    }

    public Set<VehicleService> getServices() {
        return this.services;
    }

    public void setServices(Set<VehicleService> vehicleServices) {
        if (this.services != null) {
            this.services.forEach(i -> i.setVehicle(null));
        }
        if (vehicleServices != null) {
            vehicleServices.forEach(i -> i.setVehicle(this));
        }
        this.services = vehicleServices;
    }

    public VehicleInfo services(Set<VehicleService> vehicleServices) {
        this.setServices(vehicleServices);
        return this;
    }

    public VehicleInfo addServices(VehicleService vehicleService) {
        this.services.add(vehicleService);
        vehicleService.setVehicle(this);
        return this;
    }

    public VehicleInfo removeServices(VehicleService vehicleService) {
        this.services.remove(vehicleService);
        vehicleService.setVehicle(null);
        return this;
    }

    public Set<SystemAlert> getAlerts() {
        return this.alerts;
    }

    public void setAlerts(Set<SystemAlert> systemAlerts) {
        if (this.alerts != null) {
            this.alerts.forEach(i -> i.setVehicle(null));
        }
        if (systemAlerts != null) {
            systemAlerts.forEach(i -> i.setVehicle(this));
        }
        this.alerts = systemAlerts;
    }

    public VehicleInfo alerts(Set<SystemAlert> systemAlerts) {
        this.setAlerts(systemAlerts);
        return this;
    }

    public VehicleInfo addAlerts(SystemAlert systemAlert) {
        this.alerts.add(systemAlert);
        systemAlert.setVehicle(this);
        return this;
    }

    public VehicleInfo removeAlerts(SystemAlert systemAlert) {
        this.alerts.remove(systemAlert);
        systemAlert.setVehicle(null);
        return this;
    }

    public Set<AppDevice> getDevices() {
        return this.devices;
    }

    public void setDevices(Set<AppDevice> appDevices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.setVehicle(null));
        }
        if (appDevices != null) {
            appDevices.forEach(i -> i.setVehicle(this));
        }
        this.devices = appDevices;
    }

    public VehicleInfo devices(Set<AppDevice> appDevices) {
        this.setDevices(appDevices);
        return this;
    }

    public VehicleInfo addDevices(AppDevice appDevice) {
        this.devices.add(appDevice);
        appDevice.setVehicle(this);
        return this;
    }

    public VehicleInfo removeDevices(AppDevice appDevice) {
        this.devices.remove(appDevice);
        appDevice.setVehicle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleInfo)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleInfo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleInfo{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            ", licensePlate='" + getLicensePlate() + "'" +
            ", vehicleStatus='" + getVehicleStatus() + "'" +
            "}";
    }
}
