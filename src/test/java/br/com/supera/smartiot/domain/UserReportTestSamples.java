package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserReport getUserReportSample1() {
        return new UserReport().id(1L).reportType("reportType1");
    }

    public static UserReport getUserReportSample2() {
        return new UserReport().id(2L).reportType("reportType2");
    }

    public static UserReport getUserReportRandomSampleGenerator() {
        return new UserReport().id(longCount.incrementAndGet()).reportType(UUID.randomUUID().toString());
    }
}
