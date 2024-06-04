package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleInfoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleInfo getVehicleInfoSample1() {
        return new VehicleInfo().id(1L).modelName("modelName1").licensePlate("licensePlate1");
    }

    public static VehicleInfo getVehicleInfoSample2() {
        return new VehicleInfo().id(2L).modelName("modelName2").licensePlate("licensePlate2");
    }

    public static VehicleInfo getVehicleInfoRandomSampleGenerator() {
        return new VehicleInfo()
            .id(longCount.incrementAndGet())
            .modelName(UUID.randomUUID().toString())
            .licensePlate(UUID.randomUUID().toString());
    }
}
