package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApplicationUser getApplicationUserSample1() {
        return new ApplicationUser()
            .id(1L)
            .userLogin("userLogin1")
            .firstName("firstName1")
            .lastName("lastName1")
            .emailAddress("emailAddress1");
    }

    public static ApplicationUser getApplicationUserSample2() {
        return new ApplicationUser()
            .id(2L)
            .userLogin("userLogin2")
            .firstName("firstName2")
            .lastName("lastName2")
            .emailAddress("emailAddress2");
    }

    public static ApplicationUser getApplicationUserRandomSampleGenerator() {
        return new ApplicationUser()
            .id(longCount.incrementAndGet())
            .userLogin(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .emailAddress(UUID.randomUUID().toString());
    }
}
