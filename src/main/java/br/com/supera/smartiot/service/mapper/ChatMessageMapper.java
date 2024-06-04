package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ChatMessage;
import br.com.supera.smartiot.domain.ChatSession;
import br.com.supera.smartiot.domain.ChatUser;
import br.com.supera.smartiot.service.dto.ChatMessageDTO;
import br.com.supera.smartiot.service.dto.ChatSessionDTO;
import br.com.supera.smartiot.service.dto.ChatUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatMessage} and its DTO {@link ChatMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends EntityMapper<ChatMessageDTO, ChatMessage> {
    @Mapping(target = "sender", source = "sender", qualifiedByName = "chatUserId")
    @Mapping(target = "chatSession", source = "chatSession", qualifiedByName = "chatSessionId")
    ChatMessageDTO toDto(ChatMessage s);

    @Named("chatUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatUserDTO toDtoChatUserId(ChatUser chatUser);

    @Named("chatSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatSessionDTO toDtoChatSessionId(ChatSession chatSession);
}
