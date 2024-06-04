package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleGroup getVehicleGroupSample1() {
        return new VehicleGroup().id(1L).groupName("groupName1").groupDescription("groupDescription1");
    }

    public static VehicleGroup getVehicleGroupSample2() {
        return new VehicleGroup().id(2L).groupName("groupName2").groupDescription("groupDescription2");
    }

    public static VehicleGroup getVehicleGroupRandomSampleGenerator() {
        return new VehicleGroup()
            .id(longCount.incrementAndGet())
            .groupName(UUID.randomUUID().toString())
            .groupDescription(UUID.randomUUID().toString());
    }
}
