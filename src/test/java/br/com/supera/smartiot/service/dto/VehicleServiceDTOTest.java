package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleServiceDTO.class);
        VehicleServiceDTO vehicleServiceDTO1 = new VehicleServiceDTO();
        vehicleServiceDTO1.setId(1L);
        VehicleServiceDTO vehicleServiceDTO2 = new VehicleServiceDTO();
        assertThat(vehicleServiceDTO1).isNotEqualTo(vehicleServiceDTO2);
        vehicleServiceDTO2.setId(vehicleServiceDTO1.getId());
        assertThat(vehicleServiceDTO1).isEqualTo(vehicleServiceDTO2);
        vehicleServiceDTO2.setId(2L);
        assertThat(vehicleServiceDTO1).isNotEqualTo(vehicleServiceDTO2);
        vehicleServiceDTO1.setId(null);
        assertThat(vehicleServiceDTO1).isNotEqualTo(vehicleServiceDTO2);
    }
}
