package br.com.supera.smartiot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ChatMessage.
 */
@Entity
@Table(name = "chat_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "message_id", nullable = false)
    private String messageId;

    @NotNull
    @Column(name = "message_content", nullable = false)
    private String messageContent;

    @NotNull
    @Column(name = "message_timestamp", nullable = false)
    private Instant messageTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "messages" }, allowSetters = true)
    private ChatSession chatSession;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChatMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public ChatMessage messageId(String messageId) {
        this.setMessageId(messageId);
        return this;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public ChatMessage messageContent(String messageContent) {
        this.setMessageContent(messageContent);
        return this;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Instant getMessageTimestamp() {
        return this.messageTimestamp;
    }

    public ChatMessage messageTimestamp(Instant messageTimestamp) {
        this.setMessageTimestamp(messageTimestamp);
        return this;
    }

    public void setMessageTimestamp(Instant messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public ChatUser getSender() {
        return this.sender;
    }

    public void setSender(ChatUser chatUser) {
        this.sender = chatUser;
    }

    public ChatMessage sender(ChatUser chatUser) {
        this.setSender(chatUser);
        return this;
    }

    public ChatSession getChatSession() {
        return this.chatSession;
    }

    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    public ChatMessage chatSession(ChatSession chatSession) {
        this.setChatSession(chatSession);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatMessage)) {
            return false;
        }
        return getId() != null && getId().equals(((ChatMessage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatMessage{" +
            "id=" + getId() +
            ", messageId='" + getMessageId() + "'" +
            ", messageContent='" + getMessageContent() + "'" +
            ", messageTimestamp='" + getMessageTimestamp() + "'" +
            "}";
    }
}
