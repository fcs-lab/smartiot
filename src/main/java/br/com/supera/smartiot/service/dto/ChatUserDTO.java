package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.ChatUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatUserDTO implements Serializable {

    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatUserDTO)) {
            return false;
        }

        ChatUserDTO chatUserDTO = (ChatUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatUserDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", userName='" + getUserName() + "'" +
            "}";
    }
}
