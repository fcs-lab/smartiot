package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaterUsageLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterUsageLogDTO.class);
        WaterUsageLogDTO waterUsageLogDTO1 = new WaterUsageLogDTO();
        waterUsageLogDTO1.setId(1L);
        WaterUsageLogDTO waterUsageLogDTO2 = new WaterUsageLogDTO();
        assertThat(waterUsageLogDTO1).isNotEqualTo(waterUsageLogDTO2);
        waterUsageLogDTO2.setId(waterUsageLogDTO1.getId());
        assertThat(waterUsageLogDTO1).isEqualTo(waterUsageLogDTO2);
        waterUsageLogDTO2.setId(2L);
        assertThat(waterUsageLogDTO1).isNotEqualTo(waterUsageLogDTO2);
        waterUsageLogDTO1.setId(null);
        assertThat(waterUsageLogDTO1).isNotEqualTo(waterUsageLogDTO2);
    }
}
