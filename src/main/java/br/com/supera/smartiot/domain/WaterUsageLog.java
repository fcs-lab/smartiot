package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WaterUsageLog.
 */
@Entity
@Table(name = "water_usage_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WaterUsageLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "usage_date", nullable = false)
    private Instant usageDate;

    @NotNull
    @Column(name = "amount_used", nullable = false)
    private Float amountUsed;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WaterUsageLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUsageDate() {
        return this.usageDate;
    }

    public WaterUsageLog usageDate(Instant usageDate) {
        this.setUsageDate(usageDate);
        return this;
    }

    public void setUsageDate(Instant usageDate) {
        this.usageDate = usageDate;
    }

    public Float getAmountUsed() {
        return this.amountUsed;
    }

    public WaterUsageLog amountUsed(Float amountUsed) {
        this.setAmountUsed(amountUsed);
        return this;
    }

    public void setAmountUsed(Float amountUsed) {
        this.amountUsed = amountUsed;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WaterUsageLog)) {
            return false;
        }
        return getId() != null && getId().equals(((WaterUsageLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WaterUsageLog{" +
            "id=" + getId() +
            ", usageDate='" + getUsageDate() + "'" +
            ", amountUsed=" + getAmountUsed() +
            "}";
    }
}
