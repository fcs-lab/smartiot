package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.AppDeviceTestSamples.*;
import static br.com.supera.smartiot.domain.VehicleInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppDevice.class);
        AppDevice appDevice1 = getAppDeviceSample1();
        AppDevice appDevice2 = new AppDevice();
        assertThat(appDevice1).isNotEqualTo(appDevice2);

        appDevice2.setId(appDevice1.getId());
        assertThat(appDevice1).isEqualTo(appDevice2);

        appDevice2 = getAppDeviceSample2();
        assertThat(appDevice1).isNotEqualTo(appDevice2);
    }

    @Test
    void vehicleTest() {
        AppDevice appDevice = getAppDeviceRandomSampleGenerator();
        VehicleInfo vehicleInfoBack = getVehicleInfoRandomSampleGenerator();

        appDevice.setVehicle(vehicleInfoBack);
        assertThat(appDevice.getVehicle()).isEqualTo(vehicleInfoBack);

        appDevice.vehicle(null);
        assertThat(appDevice.getVehicle()).isNull();
    }
}
