package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChatUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatUser getChatUserSample1() {
        return new ChatUser().id(1L).userId("userId1").userName("userName1");
    }

    public static ChatUser getChatUserSample2() {
        return new ChatUser().id(2L).userId("userId2").userName("userName2");
    }

    public static ChatUser getChatUserRandomSampleGenerator() {
        return new ChatUser().id(longCount.incrementAndGet()).userId(UUID.randomUUID().toString()).userName(UUID.randomUUID().toString());
    }
}
