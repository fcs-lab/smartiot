package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.ChatMessage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatMessageDTO implements Serializable {

    private Long id;

    @NotNull
    private String messageId;

    @NotNull
    private String messageContent;

    @NotNull
    private Instant messageTimestamp;

    private ChatUserDTO sender;

    private ChatSessionDTO chatSession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Instant getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(Instant messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public ChatUserDTO getSender() {
        return sender;
    }

    public void setSender(ChatUserDTO sender) {
        this.sender = sender;
    }

    public ChatSessionDTO getChatSession() {
        return chatSession;
    }

    public void setChatSession(ChatSessionDTO chatSession) {
        this.chatSession = chatSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatMessageDTO)) {
            return false;
        }

        ChatMessageDTO chatMessageDTO = (ChatMessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatMessageDTO{" +
            "id=" + getId() +
            ", messageId='" + getMessageId() + "'" +
            ", messageContent='" + getMessageContent() + "'" +
            ", messageTimestamp='" + getMessageTimestamp() + "'" +
            ", sender=" + getSender() +
            ", chatSession=" + getChatSession() +
            "}";
    }
}
