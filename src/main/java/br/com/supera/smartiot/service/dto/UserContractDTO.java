package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.UserContract} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserContractDTO implements Serializable {

    private Long id;

    @NotNull
    private String contractName;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;

    private Set<ApplicationUserDTO> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Set<ApplicationUserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<ApplicationUserDTO> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserContractDTO)) {
            return false;
        }

        UserContractDTO userContractDTO = (UserContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userContractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserContractDTO{" +
            "id=" + getId() +
            ", contractName='" + getContractName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", users=" + getUsers() +
            "}";
    }
}
