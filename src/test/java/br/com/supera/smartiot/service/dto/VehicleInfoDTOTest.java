package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleInfoDTO.class);
        VehicleInfoDTO vehicleInfoDTO1 = new VehicleInfoDTO();
        vehicleInfoDTO1.setId(1L);
        VehicleInfoDTO vehicleInfoDTO2 = new VehicleInfoDTO();
        assertThat(vehicleInfoDTO1).isNotEqualTo(vehicleInfoDTO2);
        vehicleInfoDTO2.setId(vehicleInfoDTO1.getId());
        assertThat(vehicleInfoDTO1).isEqualTo(vehicleInfoDTO2);
        vehicleInfoDTO2.setId(2L);
        assertThat(vehicleInfoDTO1).isNotEqualTo(vehicleInfoDTO2);
        vehicleInfoDTO1.setId(null);
        assertThat(vehicleInfoDTO1).isNotEqualTo(vehicleInfoDTO2);
    }
}
