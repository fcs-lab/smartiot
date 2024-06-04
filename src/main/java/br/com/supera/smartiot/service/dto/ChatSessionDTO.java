package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.ChatSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatSessionDTO implements Serializable {

    private Long id;

    @NotNull
    private String sessionId;

    @NotNull
    private Instant startTime;

    private Instant endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatSessionDTO)) {
            return false;
        }

        ChatSessionDTO chatSessionDTO = (ChatSessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatSessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatSessionDTO{" +
            "id=" + getId() +
            ", sessionId='" + getSessionId() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
