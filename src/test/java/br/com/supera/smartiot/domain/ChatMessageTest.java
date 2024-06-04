package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ChatMessageTestSamples.*;
import static br.com.supera.smartiot.domain.ChatSessionTestSamples.*;
import static br.com.supera.smartiot.domain.ChatUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessage.class);
        ChatMessage chatMessage1 = getChatMessageSample1();
        ChatMessage chatMessage2 = new ChatMessage();
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);

        chatMessage2.setId(chatMessage1.getId());
        assertThat(chatMessage1).isEqualTo(chatMessage2);

        chatMessage2 = getChatMessageSample2();
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
    }

    @Test
    void senderTest() {
        ChatMessage chatMessage = getChatMessageRandomSampleGenerator();
        ChatUser chatUserBack = getChatUserRandomSampleGenerator();

        chatMessage.setSender(chatUserBack);
        assertThat(chatMessage.getSender()).isEqualTo(chatUserBack);

        chatMessage.sender(null);
        assertThat(chatMessage.getSender()).isNull();
    }

    @Test
    void chatSessionTest() {
        ChatMessage chatMessage = getChatMessageRandomSampleGenerator();
        ChatSession chatSessionBack = getChatSessionRandomSampleGenerator();

        chatMessage.setChatSession(chatSessionBack);
        assertThat(chatMessage.getChatSession()).isEqualTo(chatSessionBack);

        chatMessage.chatSession(null);
        assertThat(chatMessage.getChatSession()).isNull();
    }
}
