package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.VehicleGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleGroup.class);
        VehicleGroup vehicleGroup1 = getVehicleGroupSample1();
        VehicleGroup vehicleGroup2 = new VehicleGroup();
        assertThat(vehicleGroup1).isNotEqualTo(vehicleGroup2);

        vehicleGroup2.setId(vehicleGroup1.getId());
        assertThat(vehicleGroup1).isEqualTo(vehicleGroup2);

        vehicleGroup2 = getVehicleGroupSample2();
        assertThat(vehicleGroup1).isNotEqualTo(vehicleGroup2);
    }
}
