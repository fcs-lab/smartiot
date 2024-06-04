package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleManufacturerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleManufacturerDTO.class);
        VehicleManufacturerDTO vehicleManufacturerDTO1 = new VehicleManufacturerDTO();
        vehicleManufacturerDTO1.setId(1L);
        VehicleManufacturerDTO vehicleManufacturerDTO2 = new VehicleManufacturerDTO();
        assertThat(vehicleManufacturerDTO1).isNotEqualTo(vehicleManufacturerDTO2);
        vehicleManufacturerDTO2.setId(vehicleManufacturerDTO1.getId());
        assertThat(vehicleManufacturerDTO1).isEqualTo(vehicleManufacturerDTO2);
        vehicleManufacturerDTO2.setId(2L);
        assertThat(vehicleManufacturerDTO1).isNotEqualTo(vehicleManufacturerDTO2);
        vehicleManufacturerDTO1.setId(null);
        assertThat(vehicleManufacturerDTO1).isNotEqualTo(vehicleManufacturerDTO2);
    }
}
