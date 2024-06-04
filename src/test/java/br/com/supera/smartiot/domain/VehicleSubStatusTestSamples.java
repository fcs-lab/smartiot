package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleSubStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleSubStatus getVehicleSubStatusSample1() {
        return new VehicleSubStatus().id(1L).subStatusName("subStatusName1");
    }

    public static VehicleSubStatus getVehicleSubStatusSample2() {
        return new VehicleSubStatus().id(2L).subStatusName("subStatusName2");
    }

    public static VehicleSubStatus getVehicleSubStatusRandomSampleGenerator() {
        return new VehicleSubStatus().id(longCount.incrementAndGet()).subStatusName(UUID.randomUUID().toString());
    }
}
