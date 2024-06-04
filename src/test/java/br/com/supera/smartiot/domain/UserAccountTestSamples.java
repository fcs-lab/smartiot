package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserAccount getUserAccountSample1() {
        return new UserAccount()
            .id(1L)
            .accountName("accountName1")
            .emailAddress("emailAddress1")
            .mobilePhone("mobilePhone1")
            .rpushFeedbackId(1)
            .employerName("employerName1")
            .pushConfiguration(1)
            .language("language1")
            .blockedReason("blockedReason1")
            .blockedById(1L)
            .deletedReason("deletedReason1")
            .deletedById(1L)
            .lastModifiedBy("lastModifiedBy1")
            .registrationCode("registrationCode1")
            .password("password1")
            .passwordHint("passwordHint1")
            .featureFlags("featureFlags1")
            .zipCode("zipCode1")
            .publicPlace("publicPlace1")
            .addressNumber("addressNumber1")
            .streetName("streetName1")
            .addressComplement("addressComplement1")
            .cityName("cityName1")
            .stateName("stateName1")
            .analyzedBy("analyzedBy1");
    }

    public static UserAccount getUserAccountSample2() {
        return new UserAccount()
            .id(2L)
            .accountName("accountName2")
            .emailAddress("emailAddress2")
            .mobilePhone("mobilePhone2")
            .rpushFeedbackId(2)
            .employerName("employerName2")
            .pushConfiguration(2)
            .language("language2")
            .blockedReason("blockedReason2")
            .blockedById(2L)
            .deletedReason("deletedReason2")
            .deletedById(2L)
            .lastModifiedBy("lastModifiedBy2")
            .registrationCode("registrationCode2")
            .password("password2")
            .passwordHint("passwordHint2")
            .featureFlags("featureFlags2")
            .zipCode("zipCode2")
            .publicPlace("publicPlace2")
            .addressNumber("addressNumber2")
            .streetName("streetName2")
            .addressComplement("addressComplement2")
            .cityName("cityName2")
            .stateName("stateName2")
            .analyzedBy("analyzedBy2");
    }

    public static UserAccount getUserAccountRandomSampleGenerator() {
        return new UserAccount()
            .id(longCount.incrementAndGet())
            .accountName(UUID.randomUUID().toString())
            .emailAddress(UUID.randomUUID().toString())
            .mobilePhone(UUID.randomUUID().toString())
            .rpushFeedbackId(intCount.incrementAndGet())
            .employerName(UUID.randomUUID().toString())
            .pushConfiguration(intCount.incrementAndGet())
            .language(UUID.randomUUID().toString())
            .blockedReason(UUID.randomUUID().toString())
            .blockedById(longCount.incrementAndGet())
            .deletedReason(UUID.randomUUID().toString())
            .deletedById(longCount.incrementAndGet())
            .lastModifiedBy(UUID.randomUUID().toString())
            .registrationCode(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .passwordHint(UUID.randomUUID().toString())
            .featureFlags(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .publicPlace(UUID.randomUUID().toString())
            .addressNumber(UUID.randomUUID().toString())
            .streetName(UUID.randomUUID().toString())
            .addressComplement(UUID.randomUUID().toString())
            .cityName(UUID.randomUUID().toString())
            .stateName(UUID.randomUUID().toString())
            .analyzedBy(UUID.randomUUID().toString());
    }
}
