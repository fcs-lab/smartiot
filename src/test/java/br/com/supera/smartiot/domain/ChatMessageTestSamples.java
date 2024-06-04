package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChatMessageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatMessage getChatMessageSample1() {
        return new ChatMessage().id(1L).messageId("messageId1").messageContent("messageContent1");
    }

    public static ChatMessage getChatMessageSample2() {
        return new ChatMessage().id(2L).messageId("messageId2").messageContent("messageContent2");
    }

    public static ChatMessage getChatMessageRandomSampleGenerator() {
        return new ChatMessage()
            .id(longCount.incrementAndGet())
            .messageId(UUID.randomUUID().toString())
            .messageContent(UUID.randomUUID().toString());
    }
}
