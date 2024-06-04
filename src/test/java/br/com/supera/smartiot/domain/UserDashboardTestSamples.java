package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserDashboardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserDashboard getUserDashboardSample1() {
        return new UserDashboard().id(1L).dashboardName("dashboardName1");
    }

    public static UserDashboard getUserDashboardSample2() {
        return new UserDashboard().id(2L).dashboardName("dashboardName2");
    }

    public static UserDashboard getUserDashboardRandomSampleGenerator() {
        return new UserDashboard().id(longCount.incrementAndGet()).dashboardName(UUID.randomUUID().toString());
    }
}
