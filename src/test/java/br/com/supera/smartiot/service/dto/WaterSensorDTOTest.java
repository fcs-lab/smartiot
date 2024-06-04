package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaterSensorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterSensorDTO.class);
        WaterSensorDTO waterSensorDTO1 = new WaterSensorDTO();
        waterSensorDTO1.setId(1L);
        WaterSensorDTO waterSensorDTO2 = new WaterSensorDTO();
        assertThat(waterSensorDTO1).isNotEqualTo(waterSensorDTO2);
        waterSensorDTO2.setId(waterSensorDTO1.getId());
        assertThat(waterSensorDTO1).isEqualTo(waterSensorDTO2);
        waterSensorDTO2.setId(2L);
        assertThat(waterSensorDTO1).isNotEqualTo(waterSensorDTO2);
        waterSensorDTO1.setId(null);
        assertThat(waterSensorDTO1).isNotEqualTo(waterSensorDTO2);
    }
}
