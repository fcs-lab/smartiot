package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ChatUserAsserts.*;
import static br.com.supera.smartiot.domain.ChatUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatUserMapperTest {

    private ChatUserMapper chatUserMapper;

    @BeforeEach
    void setUp() {
        chatUserMapper = new ChatUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatUserSample1();
        var actual = chatUserMapper.toEntity(chatUserMapper.toDto(expected));
        assertChatUserAllPropertiesEquals(expected, actual);
    }
}
