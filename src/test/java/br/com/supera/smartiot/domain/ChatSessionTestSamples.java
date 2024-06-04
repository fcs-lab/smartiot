package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChatSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatSession getChatSessionSample1() {
        return new ChatSession().id(1L).sessionId("sessionId1");
    }

    public static ChatSession getChatSessionSample2() {
        return new ChatSession().id(2L).sessionId("sessionId2");
    }

    public static ChatSession getChatSessionRandomSampleGenerator() {
        return new ChatSession().id(longCount.incrementAndGet()).sessionId(UUID.randomUUID().toString());
    }
}
