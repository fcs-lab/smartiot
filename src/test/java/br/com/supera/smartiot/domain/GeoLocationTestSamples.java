package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GeoLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static GeoLocation getGeoLocationSample1() {
        return new GeoLocation().id(1L).fullAddress("fullAddress1");
    }

    public static GeoLocation getGeoLocationSample2() {
        return new GeoLocation().id(2L).fullAddress("fullAddress2");
    }

    public static GeoLocation getGeoLocationRandomSampleGenerator() {
        return new GeoLocation().id(longCount.incrementAndGet()).fullAddress(UUID.randomUUID().toString());
    }
}
