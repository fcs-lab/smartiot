package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.VehicleModelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleModel.class);
        VehicleModel vehicleModel1 = getVehicleModelSample1();
        VehicleModel vehicleModel2 = new VehicleModel();
        assertThat(vehicleModel1).isNotEqualTo(vehicleModel2);

        vehicleModel2.setId(vehicleModel1.getId());
        assertThat(vehicleModel1).isEqualTo(vehicleModel2);

        vehicleModel2 = getVehicleModelSample2();
        assertThat(vehicleModel1).isNotEqualTo(vehicleModel2);
    }
}
