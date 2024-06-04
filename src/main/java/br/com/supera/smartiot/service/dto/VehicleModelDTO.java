package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.VehicleModel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleModelDTO implements Serializable {

    private Long id;

    @NotNull
    private String modelName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleModelDTO)) {
            return false;
        }

        VehicleModelDTO vehicleModelDTO = (VehicleModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleModelDTO{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            "}";
    }
}
