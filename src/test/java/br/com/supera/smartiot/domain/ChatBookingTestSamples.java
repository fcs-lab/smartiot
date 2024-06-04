package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ChatBookingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatBooking getChatBookingSample1() {
        return new ChatBooking().id(1L);
    }

    public static ChatBooking getChatBookingSample2() {
        return new ChatBooking().id(2L);
    }

    public static ChatBooking getChatBookingRandomSampleGenerator() {
        return new ChatBooking().id(longCount.incrementAndGet());
    }
}
