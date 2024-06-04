package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.VehicleGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String groupName;

    private String groupDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleGroupDTO)) {
            return false;
        }

        VehicleGroupDTO vehicleGroupDTO = (VehicleGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleGroupDTO{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", groupDescription='" + getGroupDescription() + "'" +
            "}";
    }
}
