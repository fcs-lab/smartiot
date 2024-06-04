package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserContractTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserContract getUserContractSample1() {
        return new UserContract().id(1L).contractName("contractName1");
    }

    public static UserContract getUserContractSample2() {
        return new UserContract().id(2L).contractName("contractName2");
    }

    public static UserContract getUserContractRandomSampleGenerator() {
        return new UserContract().id(longCount.incrementAndGet()).contractName(UUID.randomUUID().toString());
    }
}
