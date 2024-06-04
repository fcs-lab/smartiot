package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserDashboard.
 */
@Entity
@Table(name = "user_dashboard")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserDashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "dashboard_name", nullable = false)
    private String dashboardName;

    @Lob
    @Column(name = "widgets", nullable = false)
    private String widgets;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserDashboard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDashboardName() {
        return this.dashboardName;
    }

    public UserDashboard dashboardName(String dashboardName) {
        this.setDashboardName(dashboardName);
        return this;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getWidgets() {
        return this.widgets;
    }

    public UserDashboard widgets(String widgets) {
        this.setWidgets(widgets);
        return this;
    }

    public void setWidgets(String widgets) {
        this.widgets = widgets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDashboard)) {
            return false;
        }
        return getId() != null && getId().equals(((UserDashboard) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDashboard{" +
            "id=" + getId() +
            ", dashboardName='" + getDashboardName() + "'" +
            ", widgets='" + getWidgets() + "'" +
            "}";
    }
}
