package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleDamageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleDamage getVehicleDamageSample1() {
        return new VehicleDamage().id(1L).damageDescription("damageDescription1");
    }

    public static VehicleDamage getVehicleDamageSample2() {
        return new VehicleDamage().id(2L).damageDescription("damageDescription2");
    }

    public static VehicleDamage getVehicleDamageRandomSampleGenerator() {
        return new VehicleDamage().id(longCount.incrementAndGet()).damageDescription(UUID.randomUUID().toString());
    }
}
