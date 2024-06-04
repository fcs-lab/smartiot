package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CarRideTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CarRide getCarRideSample1() {
        return new CarRide().id(1L).origin("origin1").destination("destination1").availableSeats(1);
    }

    public static CarRide getCarRideSample2() {
        return new CarRide().id(2L).origin("origin2").destination("destination2").availableSeats(2);
    }

    public static CarRide getCarRideRandomSampleGenerator() {
        return new CarRide()
            .id(longCount.incrementAndGet())
            .origin(UUID.randomUUID().toString())
            .destination(UUID.randomUUID().toString())
            .availableSeats(intCount.incrementAndGet());
    }
}
