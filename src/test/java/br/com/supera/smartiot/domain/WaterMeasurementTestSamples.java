package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WaterMeasurementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WaterMeasurement getWaterMeasurementSample1() {
        return new WaterMeasurement().id(1L).waterQuality("waterQuality1");
    }

    public static WaterMeasurement getWaterMeasurementSample2() {
        return new WaterMeasurement().id(2L).waterQuality("waterQuality2");
    }

    public static WaterMeasurement getWaterMeasurementRandomSampleGenerator() {
        return new WaterMeasurement().id(longCount.incrementAndGet()).waterQuality(UUID.randomUUID().toString());
    }
}
