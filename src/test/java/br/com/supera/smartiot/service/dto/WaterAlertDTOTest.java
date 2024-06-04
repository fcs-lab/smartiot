package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaterAlertDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterAlertDTO.class);
        WaterAlertDTO waterAlertDTO1 = new WaterAlertDTO();
        waterAlertDTO1.setId(1L);
        WaterAlertDTO waterAlertDTO2 = new WaterAlertDTO();
        assertThat(waterAlertDTO1).isNotEqualTo(waterAlertDTO2);
        waterAlertDTO2.setId(waterAlertDTO1.getId());
        assertThat(waterAlertDTO1).isEqualTo(waterAlertDTO2);
        waterAlertDTO2.setId(2L);
        assertThat(waterAlertDTO1).isNotEqualTo(waterAlertDTO2);
        waterAlertDTO1.setId(null);
        assertThat(waterAlertDTO1).isNotEqualTo(waterAlertDTO2);
    }
}
