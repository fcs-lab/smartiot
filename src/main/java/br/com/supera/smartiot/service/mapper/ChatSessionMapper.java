package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ChatSession;
import br.com.supera.smartiot.service.dto.ChatSessionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatSession} and its DTO {@link ChatSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatSessionMapper extends EntityMapper<ChatSessionDTO, ChatSession> {}
