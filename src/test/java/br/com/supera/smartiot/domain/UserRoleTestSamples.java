package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserRole getUserRoleSample1() {
        return new UserRole().id(1L).roleName("roleName1");
    }

    public static UserRole getUserRoleSample2() {
        return new UserRole().id(2L).roleName("roleName2");
    }

    public static UserRole getUserRoleRandomSampleGenerator() {
        return new UserRole().id(longCount.incrementAndGet()).roleName(UUID.randomUUID().toString());
    }
}
