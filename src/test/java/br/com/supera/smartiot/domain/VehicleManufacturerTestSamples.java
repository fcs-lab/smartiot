package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleManufacturerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleManufacturer getVehicleManufacturerSample1() {
        return new VehicleManufacturer().id(1L).manufacturerName("manufacturerName1").manufacturerCountry("manufacturerCountry1");
    }

    public static VehicleManufacturer getVehicleManufacturerSample2() {
        return new VehicleManufacturer().id(2L).manufacturerName("manufacturerName2").manufacturerCountry("manufacturerCountry2");
    }

    public static VehicleManufacturer getVehicleManufacturerRandomSampleGenerator() {
        return new VehicleManufacturer()
            .id(longCount.incrementAndGet())
            .manufacturerName(UUID.randomUUID().toString())
            .manufacturerCountry(UUID.randomUUID().toString());
    }
}
