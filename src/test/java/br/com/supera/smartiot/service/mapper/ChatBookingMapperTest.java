package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ChatBookingAsserts.*;
import static br.com.supera.smartiot.domain.ChatBookingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatBookingMapperTest {

    private ChatBookingMapper chatBookingMapper;

    @BeforeEach
    void setUp() {
        chatBookingMapper = new ChatBookingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatBookingSample1();
        var actual = chatBookingMapper.toEntity(chatBookingMapper.toDto(expected));
        assertChatBookingAllPropertiesEquals(expected, actual);
    }
}
