package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CostCenterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CostCenter getCostCenterSample1() {
        return new CostCenter().id(1L).centerName("centerName1");
    }

    public static CostCenter getCostCenterSample2() {
        return new CostCenter().id(2L).centerName("centerName2");
    }

    public static CostCenter getCostCenterRandomSampleGenerator() {
        return new CostCenter().id(longCount.incrementAndGet()).centerName(UUID.randomUUID().toString());
    }
}
