package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ChatUser;
import br.com.supera.smartiot.service.dto.ChatUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatUser} and its DTO {@link ChatUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatUserMapper extends EntityMapper<ChatUserDTO, ChatUser> {}
