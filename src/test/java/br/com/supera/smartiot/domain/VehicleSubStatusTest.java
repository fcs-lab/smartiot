package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.VehicleSubStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleSubStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleSubStatus.class);
        VehicleSubStatus vehicleSubStatus1 = getVehicleSubStatusSample1();
        VehicleSubStatus vehicleSubStatus2 = new VehicleSubStatus();
        assertThat(vehicleSubStatus1).isNotEqualTo(vehicleSubStatus2);

        vehicleSubStatus2.setId(vehicleSubStatus1.getId());
        assertThat(vehicleSubStatus1).isEqualTo(vehicleSubStatus2);

        vehicleSubStatus2 = getVehicleSubStatusSample2();
        assertThat(vehicleSubStatus1).isNotEqualTo(vehicleSubStatus2);
    }
}
