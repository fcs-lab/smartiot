package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleServiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleService getVehicleServiceSample1() {
        return new VehicleService().id(1L).serviceName("serviceName1").serviceDescription("serviceDescription1");
    }

    public static VehicleService getVehicleServiceSample2() {
        return new VehicleService().id(2L).serviceName("serviceName2").serviceDescription("serviceDescription2");
    }

    public static VehicleService getVehicleServiceRandomSampleGenerator() {
        return new VehicleService()
            .id(longCount.incrementAndGet())
            .serviceName(UUID.randomUUID().toString())
            .serviceDescription(UUID.randomUUID().toString());
    }
}
