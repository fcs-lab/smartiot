package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ChatSessionAsserts.*;
import static br.com.supera.smartiot.domain.ChatSessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatSessionMapperTest {

    private ChatSessionMapper chatSessionMapper;

    @BeforeEach
    void setUp() {
        chatSessionMapper = new ChatSessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatSessionSample1();
        var actual = chatSessionMapper.toEntity(chatSessionMapper.toDto(expected));
        assertChatSessionAllPropertiesEquals(expected, actual);
    }
}
