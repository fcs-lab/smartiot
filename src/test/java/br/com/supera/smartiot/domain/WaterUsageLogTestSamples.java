package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class WaterUsageLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WaterUsageLog getWaterUsageLogSample1() {
        return new WaterUsageLog().id(1L);
    }

    public static WaterUsageLog getWaterUsageLogSample2() {
        return new WaterUsageLog().id(2L);
    }

    public static WaterUsageLog getWaterUsageLogRandomSampleGenerator() {
        return new WaterUsageLog().id(longCount.incrementAndGet());
    }
}
