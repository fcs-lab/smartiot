package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.SystemAlertTestSamples.*;
import static br.com.supera.smartiot.domain.VehicleInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemAlertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemAlert.class);
        SystemAlert systemAlert1 = getSystemAlertSample1();
        SystemAlert systemAlert2 = new SystemAlert();
        assertThat(systemAlert1).isNotEqualTo(systemAlert2);

        systemAlert2.setId(systemAlert1.getId());
        assertThat(systemAlert1).isEqualTo(systemAlert2);

        systemAlert2 = getSystemAlertSample2();
        assertThat(systemAlert1).isNotEqualTo(systemAlert2);
    }

    @Test
    void vehicleTest() {
        SystemAlert systemAlert = getSystemAlertRandomSampleGenerator();
        VehicleInfo vehicleInfoBack = getVehicleInfoRandomSampleGenerator();

        systemAlert.setVehicle(vehicleInfoBack);
        assertThat(systemAlert.getVehicle()).isEqualTo(vehicleInfoBack);

        systemAlert.vehicle(null);
        assertThat(systemAlert.getVehicle()).isNull();
    }
}
