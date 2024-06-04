package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ApplicationUserTestSamples.*;
import static br.com.supera.smartiot.domain.CarRideTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarRideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarRide.class);
        CarRide carRide1 = getCarRideSample1();
        CarRide carRide2 = new CarRide();
        assertThat(carRide1).isNotEqualTo(carRide2);

        carRide2.setId(carRide1.getId());
        assertThat(carRide1).isEqualTo(carRide2);

        carRide2 = getCarRideSample2();
        assertThat(carRide1).isNotEqualTo(carRide2);
    }

    @Test
    void driverTest() {
        CarRide carRide = getCarRideRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        carRide.setDriver(applicationUserBack);
        assertThat(carRide.getDriver()).isEqualTo(applicationUserBack);

        carRide.driver(null);
        assertThat(carRide.getDriver()).isNull();
    }
}
