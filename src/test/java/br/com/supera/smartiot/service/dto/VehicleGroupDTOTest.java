package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleGroupDTO.class);
        VehicleGroupDTO vehicleGroupDTO1 = new VehicleGroupDTO();
        vehicleGroupDTO1.setId(1L);
        VehicleGroupDTO vehicleGroupDTO2 = new VehicleGroupDTO();
        assertThat(vehicleGroupDTO1).isNotEqualTo(vehicleGroupDTO2);
        vehicleGroupDTO2.setId(vehicleGroupDTO1.getId());
        assertThat(vehicleGroupDTO1).isEqualTo(vehicleGroupDTO2);
        vehicleGroupDTO2.setId(2L);
        assertThat(vehicleGroupDTO1).isNotEqualTo(vehicleGroupDTO2);
        vehicleGroupDTO1.setId(null);
        assertThat(vehicleGroupDTO1).isNotEqualTo(vehicleGroupDTO2);
    }
}
