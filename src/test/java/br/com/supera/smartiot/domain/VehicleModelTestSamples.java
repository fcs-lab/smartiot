package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleModelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleModel getVehicleModelSample1() {
        return new VehicleModel().id(1L).modelName("modelName1");
    }

    public static VehicleModel getVehicleModelSample2() {
        return new VehicleModel().id(2L).modelName("modelName2");
    }

    public static VehicleModel getVehicleModelRandomSampleGenerator() {
        return new VehicleModel().id(longCount.incrementAndGet()).modelName(UUID.randomUUID().toString());
    }
}
