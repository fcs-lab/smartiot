package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.VehicleSubStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleSubStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String subStatusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubStatusName() {
        return subStatusName;
    }

    public void setSubStatusName(String subStatusName) {
        this.subStatusName = subStatusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleSubStatusDTO)) {
            return false;
        }

        VehicleSubStatusDTO vehicleSubStatusDTO = (VehicleSubStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleSubStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleSubStatusDTO{" +
            "id=" + getId() +
            ", subStatusName='" + getSubStatusName() + "'" +
            "}";
    }
}
