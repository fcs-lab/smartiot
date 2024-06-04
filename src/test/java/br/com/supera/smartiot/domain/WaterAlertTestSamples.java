package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WaterAlertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WaterAlert getWaterAlertSample1() {
        return new WaterAlert().id(1L).alertType("alertType1").alertDescription("alertDescription1");
    }

    public static WaterAlert getWaterAlertSample2() {
        return new WaterAlert().id(2L).alertType("alertType2").alertDescription("alertDescription2");
    }

    public static WaterAlert getWaterAlertRandomSampleGenerator() {
        return new WaterAlert()
            .id(longCount.incrementAndGet())
            .alertType(UUID.randomUUID().toString())
            .alertDescription(UUID.randomUUID().toString());
    }
}
