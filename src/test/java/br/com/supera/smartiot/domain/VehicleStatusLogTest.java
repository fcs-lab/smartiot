package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.VehicleStatusLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleStatusLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleStatusLog.class);
        VehicleStatusLog vehicleStatusLog1 = getVehicleStatusLogSample1();
        VehicleStatusLog vehicleStatusLog2 = new VehicleStatusLog();
        assertThat(vehicleStatusLog1).isNotEqualTo(vehicleStatusLog2);

        vehicleStatusLog2.setId(vehicleStatusLog1.getId());
        assertThat(vehicleStatusLog1).isEqualTo(vehicleStatusLog2);

        vehicleStatusLog2 = getVehicleStatusLogSample2();
        assertThat(vehicleStatusLog1).isNotEqualTo(vehicleStatusLog2);
    }
}
