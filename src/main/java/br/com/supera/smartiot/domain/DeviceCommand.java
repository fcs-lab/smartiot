package br.com.supera.smartiot.domain;

import br.com.supera.smartiot.domain.enumeration.CommandStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeviceCommand.
 */
@Entity
@Table(name = "device_command")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "command_type", nullable = false)
    private String commandType;

    @NotNull
    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;

    @Column(name = "executed_at")
    private Instant executedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "command_status", nullable = false)
    private CommandStatus commandStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeviceCommand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandType() {
        return this.commandType;
    }

    public DeviceCommand commandType(String commandType) {
        this.setCommandType(commandType);
        return this;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public Instant getSentAt() {
        return this.sentAt;
    }

    public DeviceCommand sentAt(Instant sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getExecutedAt() {
        return this.executedAt;
    }

    public DeviceCommand executedAt(Instant executedAt) {
        this.setExecutedAt(executedAt);
        return this;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }

    public CommandStatus getCommandStatus() {
        return this.commandStatus;
    }

    public DeviceCommand commandStatus(CommandStatus commandStatus) {
        this.setCommandStatus(commandStatus);
        return this;
    }

    public void setCommandStatus(CommandStatus commandStatus) {
        this.commandStatus = commandStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceCommand)) {
            return false;
        }
        return getId() != null && getId().equals(((DeviceCommand) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceCommand{" +
            "id=" + getId() +
            ", commandType='" + getCommandType() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", executedAt='" + getExecutedAt() + "'" +
            ", commandStatus='" + getCommandStatus() + "'" +
            "}";
    }
}
