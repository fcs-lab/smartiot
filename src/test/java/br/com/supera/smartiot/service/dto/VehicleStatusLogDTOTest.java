package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleStatusLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleStatusLogDTO.class);
        VehicleStatusLogDTO vehicleStatusLogDTO1 = new VehicleStatusLogDTO();
        vehicleStatusLogDTO1.setId(1L);
        VehicleStatusLogDTO vehicleStatusLogDTO2 = new VehicleStatusLogDTO();
        assertThat(vehicleStatusLogDTO1).isNotEqualTo(vehicleStatusLogDTO2);
        vehicleStatusLogDTO2.setId(vehicleStatusLogDTO1.getId());
        assertThat(vehicleStatusLogDTO1).isEqualTo(vehicleStatusLogDTO2);
        vehicleStatusLogDTO2.setId(2L);
        assertThat(vehicleStatusLogDTO1).isNotEqualTo(vehicleStatusLogDTO2);
        vehicleStatusLogDTO1.setId(null);
        assertThat(vehicleStatusLogDTO1).isNotEqualTo(vehicleStatusLogDTO2);
    }
}
