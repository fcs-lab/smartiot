package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleSubStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleSubStatusDTO.class);
        VehicleSubStatusDTO vehicleSubStatusDTO1 = new VehicleSubStatusDTO();
        vehicleSubStatusDTO1.setId(1L);
        VehicleSubStatusDTO vehicleSubStatusDTO2 = new VehicleSubStatusDTO();
        assertThat(vehicleSubStatusDTO1).isNotEqualTo(vehicleSubStatusDTO2);
        vehicleSubStatusDTO2.setId(vehicleSubStatusDTO1.getId());
        assertThat(vehicleSubStatusDTO1).isEqualTo(vehicleSubStatusDTO2);
        vehicleSubStatusDTO2.setId(2L);
        assertThat(vehicleSubStatusDTO1).isNotEqualTo(vehicleSubStatusDTO2);
        vehicleSubStatusDTO1.setId(null);
        assertThat(vehicleSubStatusDTO1).isNotEqualTo(vehicleSubStatusDTO2);
    }
}
