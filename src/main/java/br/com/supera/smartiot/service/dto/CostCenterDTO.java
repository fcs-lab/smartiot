package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.CostCenter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CostCenterDTO implements Serializable {

    private Long id;

    @NotNull
    private String centerName;

    @NotNull
    private Float budgetAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public Float getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Float budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CostCenterDTO)) {
            return false;
        }

        CostCenterDTO costCenterDTO = (CostCenterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, costCenterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CostCenterDTO{" +
            "id=" + getId() +
            ", centerName='" + getCenterName() + "'" +
            ", budgetAmount=" + getBudgetAmount() +
            "}";
    }
}
