package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleDamageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleDamageDTO.class);
        VehicleDamageDTO vehicleDamageDTO1 = new VehicleDamageDTO();
        vehicleDamageDTO1.setId(1L);
        VehicleDamageDTO vehicleDamageDTO2 = new VehicleDamageDTO();
        assertThat(vehicleDamageDTO1).isNotEqualTo(vehicleDamageDTO2);
        vehicleDamageDTO2.setId(vehicleDamageDTO1.getId());
        assertThat(vehicleDamageDTO1).isEqualTo(vehicleDamageDTO2);
        vehicleDamageDTO2.setId(2L);
        assertThat(vehicleDamageDTO1).isNotEqualTo(vehicleDamageDTO2);
        vehicleDamageDTO1.setId(null);
        assertThat(vehicleDamageDTO1).isNotEqualTo(vehicleDamageDTO2);
    }
}
