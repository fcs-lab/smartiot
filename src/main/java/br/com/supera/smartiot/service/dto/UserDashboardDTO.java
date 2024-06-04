package br.com.supera.smartiot.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.UserDashboard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserDashboardDTO implements Serializable {

    private Long id;

    @NotNull
    private String dashboardName;

    @Lob
    private String widgets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getWidgets() {
        return widgets;
    }

    public void setWidgets(String widgets) {
        this.widgets = widgets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDashboardDTO)) {
            return false;
        }

        UserDashboardDTO userDashboardDTO = (UserDashboardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userDashboardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDashboardDTO{" +
            "id=" + getId() +
            ", dashboardName='" + getDashboardName() + "'" +
            ", widgets='" + getWidgets() + "'" +
            "}";
    }
}
