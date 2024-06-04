package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleModelDTO.class);
        VehicleModelDTO vehicleModelDTO1 = new VehicleModelDTO();
        vehicleModelDTO1.setId(1L);
        VehicleModelDTO vehicleModelDTO2 = new VehicleModelDTO();
        assertThat(vehicleModelDTO1).isNotEqualTo(vehicleModelDTO2);
        vehicleModelDTO2.setId(vehicleModelDTO1.getId());
        assertThat(vehicleModelDTO1).isEqualTo(vehicleModelDTO2);
        vehicleModelDTO2.setId(2L);
        assertThat(vehicleModelDTO1).isNotEqualTo(vehicleModelDTO2);
        vehicleModelDTO1.setId(null);
        assertThat(vehicleModelDTO1).isNotEqualTo(vehicleModelDTO2);
    }
}
