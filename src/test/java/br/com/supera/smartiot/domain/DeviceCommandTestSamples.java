package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceCommandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DeviceCommand getDeviceCommandSample1() {
        return new DeviceCommand().id(1L).commandType("commandType1");
    }

    public static DeviceCommand getDeviceCommandSample2() {
        return new DeviceCommand().id(2L).commandType("commandType2");
    }

    public static DeviceCommand getDeviceCommandRandomSampleGenerator() {
        return new DeviceCommand().id(longCount.incrementAndGet()).commandType(UUID.randomUUID().toString());
    }
}
