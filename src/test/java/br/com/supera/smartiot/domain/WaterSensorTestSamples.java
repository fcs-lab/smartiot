package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WaterSensorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WaterSensor getWaterSensorSample1() {
        return new WaterSensor().id(1L).sensorId("sensorId1");
    }

    public static WaterSensor getWaterSensorSample2() {
        return new WaterSensor().id(2L).sensorId("sensorId2");
    }

    public static WaterSensor getWaterSensorRandomSampleGenerator() {
        return new WaterSensor().id(longCount.incrementAndGet()).sensorId(UUID.randomUUID().toString());
    }
}
