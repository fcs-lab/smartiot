package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.VehicleManufacturer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleManufacturerDTO implements Serializable {

    private Long id;

    @NotNull
    private String manufacturerName;

    @NotNull
    private String manufacturerCountry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerCountry() {
        return manufacturerCountry;
    }

    public void setManufacturerCountry(String manufacturerCountry) {
        this.manufacturerCountry = manufacturerCountry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleManufacturerDTO)) {
            return false;
        }

        VehicleManufacturerDTO vehicleManufacturerDTO = (VehicleManufacturerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleManufacturerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleManufacturerDTO{" +
            "id=" + getId() +
            ", manufacturerName='" + getManufacturerName() + "'" +
            ", manufacturerCountry='" + getManufacturerCountry() + "'" +
            "}";
    }
}
