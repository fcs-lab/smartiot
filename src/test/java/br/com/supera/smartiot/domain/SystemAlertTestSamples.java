package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SystemAlertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SystemAlert getSystemAlertSample1() {
        return new SystemAlert().id(1L).alertDescription("alertDescription1").alertType("alertType1");
    }

    public static SystemAlert getSystemAlertSample2() {
        return new SystemAlert().id(2L).alertDescription("alertDescription2").alertType("alertType2");
    }

    public static SystemAlert getSystemAlertRandomSampleGenerator() {
        return new SystemAlert()
            .id(longCount.incrementAndGet())
            .alertDescription(UUID.randomUUID().toString())
            .alertType(UUID.randomUUID().toString());
    }
}
