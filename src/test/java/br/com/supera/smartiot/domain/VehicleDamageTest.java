package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.VehicleDamageTestSamples.*;
import static br.com.supera.smartiot.domain.VehicleInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleDamageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleDamage.class);
        VehicleDamage vehicleDamage1 = getVehicleDamageSample1();
        VehicleDamage vehicleDamage2 = new VehicleDamage();
        assertThat(vehicleDamage1).isNotEqualTo(vehicleDamage2);

        vehicleDamage2.setId(vehicleDamage1.getId());
        assertThat(vehicleDamage1).isEqualTo(vehicleDamage2);

        vehicleDamage2 = getVehicleDamageSample2();
        assertThat(vehicleDamage1).isNotEqualTo(vehicleDamage2);
    }

    @Test
    void vehicleTest() {
        VehicleDamage vehicleDamage = getVehicleDamageRandomSampleGenerator();
        VehicleInfo vehicleInfoBack = getVehicleInfoRandomSampleGenerator();

        vehicleDamage.setVehicle(vehicleInfoBack);
        assertThat(vehicleDamage.getVehicle()).isEqualTo(vehicleInfoBack);

        vehicleDamage.vehicle(null);
        assertThat(vehicleDamage.getVehicle()).isNull();
    }
}
