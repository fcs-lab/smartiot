package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.WaterUsageLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaterUsageLogDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant usageDate;

    @NotNull
    private Float amountUsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(Instant usageDate) {
        this.usageDate = usageDate;
    }

    public Float getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(Float amountUsed) {
        this.amountUsed = amountUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaterUsageLogDTO)) {
            return false;
        }

        WaterUsageLogDTO waterUsageLogDTO = (WaterUsageLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, waterUsageLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaterUsageLogDTO{" +
            "id=" + getId() +
            ", usageDate='" + getUsageDate() + "'" +
            ", amountUsed=" + getAmountUsed() +
            "}";
    }
}
