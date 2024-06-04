package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleManufacturer.
 */
@Entity
@Table(name = "vehicle_manufacturer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleManufacturer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;

    @NotNull
    @Column(name = "manufacturer_country", nullable = false)
    private String manufacturerCountry;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleManufacturer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return this.manufacturerName;
    }

    public VehicleManufacturer manufacturerName(String manufacturerName) {
        this.setManufacturerName(manufacturerName);
        return this;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerCountry() {
        return this.manufacturerCountry;
    }

    public VehicleManufacturer manufacturerCountry(String manufacturerCountry) {
        this.setManufacturerCountry(manufacturerCountry);
        return this;
    }

    public void setManufacturerCountry(String manufacturerCountry) {
        this.manufacturerCountry = manufacturerCountry;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleManufacturer)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleManufacturer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleManufacturer{" +
            "id=" + getId() +
            ", manufacturerName='" + getManufacturerName() + "'" +
            ", manufacturerCountry='" + getManufacturerCountry() + "'" +
            "}";
    }
}
