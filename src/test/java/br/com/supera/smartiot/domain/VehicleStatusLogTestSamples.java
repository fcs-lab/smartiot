package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleStatusLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleStatusLog getVehicleStatusLogSample1() {
        return new VehicleStatusLog().id(1L);
    }

    public static VehicleStatusLog getVehicleStatusLogSample2() {
        return new VehicleStatusLog().id(2L);
    }

    public static VehicleStatusLog getVehicleStatusLogRandomSampleGenerator() {
        return new VehicleStatusLog().id(longCount.incrementAndGet());
    }
}
