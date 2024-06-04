package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceTelemetryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DeviceTelemetry getDeviceTelemetrySample1() {
        return new DeviceTelemetry().id(1L).engineStatus("engineStatus1");
    }

    public static DeviceTelemetry getDeviceTelemetrySample2() {
        return new DeviceTelemetry().id(2L).engineStatus("engineStatus2");
    }

    public static DeviceTelemetry getDeviceTelemetryRandomSampleGenerator() {
        return new DeviceTelemetry().id(longCount.incrementAndGet()).engineStatus(UUID.randomUUID().toString());
    }
}
