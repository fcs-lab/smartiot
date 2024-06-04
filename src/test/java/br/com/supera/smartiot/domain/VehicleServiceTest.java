package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.VehicleInfoTestSamples.*;
import static br.com.supera.smartiot.domain.VehicleServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleService.class);
        VehicleService vehicleService1 = getVehicleServiceSample1();
        VehicleService vehicleService2 = new VehicleService();
        assertThat(vehicleService1).isNotEqualTo(vehicleService2);

        vehicleService2.setId(vehicleService1.getId());
        assertThat(vehicleService1).isEqualTo(vehicleService2);

        vehicleService2 = getVehicleServiceSample2();
        assertThat(vehicleService1).isNotEqualTo(vehicleService2);
    }

    @Test
    void vehicleTest() {
        VehicleService vehicleService = getVehicleServiceRandomSampleGenerator();
        VehicleInfo vehicleInfoBack = getVehicleInfoRandomSampleGenerator();

        vehicleService.setVehicle(vehicleInfoBack);
        assertThat(vehicleService.getVehicle()).isEqualTo(vehicleInfoBack);

        vehicleService.vehicle(null);
        assertThat(vehicleService.getVehicle()).isNull();
    }
}
