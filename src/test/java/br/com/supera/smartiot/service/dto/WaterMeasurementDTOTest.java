package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaterMeasurementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterMeasurementDTO.class);
        WaterMeasurementDTO waterMeasurementDTO1 = new WaterMeasurementDTO();
        waterMeasurementDTO1.setId(1L);
        WaterMeasurementDTO waterMeasurementDTO2 = new WaterMeasurementDTO();
        assertThat(waterMeasurementDTO1).isNotEqualTo(waterMeasurementDTO2);
        waterMeasurementDTO2.setId(waterMeasurementDTO1.getId());
        assertThat(waterMeasurementDTO1).isEqualTo(waterMeasurementDTO2);
        waterMeasurementDTO2.setId(2L);
        assertThat(waterMeasurementDTO1).isNotEqualTo(waterMeasurementDTO2);
        waterMeasurementDTO1.setId(null);
        assertThat(waterMeasurementDTO1).isNotEqualTo(waterMeasurementDTO2);
    }
}
