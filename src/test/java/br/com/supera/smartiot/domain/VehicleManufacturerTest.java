package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.VehicleManufacturerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleManufacturerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleManufacturer.class);
        VehicleManufacturer vehicleManufacturer1 = getVehicleManufacturerSample1();
        VehicleManufacturer vehicleManufacturer2 = new VehicleManufacturer();
        assertThat(vehicleManufacturer1).isNotEqualTo(vehicleManufacturer2);

        vehicleManufacturer2.setId(vehicleManufacturer1.getId());
        assertThat(vehicleManufacturer1).isEqualTo(vehicleManufacturer2);

        vehicleManufacturer2 = getVehicleManufacturerSample2();
        assertThat(vehicleManufacturer1).isNotEqualTo(vehicleManufacturer2);
    }
}
