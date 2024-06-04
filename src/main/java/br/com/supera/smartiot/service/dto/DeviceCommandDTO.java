package br.com.supera.smartiot.service.dto;

import br.com.supera.smartiot.domain.enumeration.CommandStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.DeviceCommand} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceCommandDTO implements Serializable {

    private Long id;

    @NotNull
    private String commandType;

    @NotNull
    private Instant sentAt;

    private Instant executedAt;

    @NotNull
    private CommandStatus commandStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }

    public CommandStatus getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(CommandStatus commandStatus) {
        this.commandStatus = commandStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceCommandDTO)) {
            return false;
        }

        DeviceCommandDTO deviceCommandDTO = (DeviceCommandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceCommandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceCommandDTO{" +
            "id=" + getId() +
            ", commandType='" + getCommandType() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", executedAt='" + getExecutedAt() + "'" +
            ", commandStatus='" + getCommandStatus() + "'" +
            "}";
    }
}
