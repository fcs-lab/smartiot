package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppDeviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppDevice getAppDeviceSample1() {
        return new AppDevice().id(1L).deviceId("deviceId1");
    }

    public static AppDevice getAppDeviceSample2() {
        return new AppDevice().id(2L).deviceId("deviceId2");
    }

    public static AppDevice getAppDeviceRandomSampleGenerator() {
        return new AppDevice().id(longCount.incrementAndGet()).deviceId(UUID.randomUUID().toString());
    }
}
