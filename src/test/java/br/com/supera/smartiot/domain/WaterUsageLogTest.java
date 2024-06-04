package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.WaterUsageLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaterUsageLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterUsageLog.class);
        WaterUsageLog waterUsageLog1 = getWaterUsageLogSample1();
        WaterUsageLog waterUsageLog2 = new WaterUsageLog();
        assertThat(waterUsageLog1).isNotEqualTo(waterUsageLog2);

        waterUsageLog2.setId(waterUsageLog1.getId());
        assertThat(waterUsageLog1).isEqualTo(waterUsageLog2);

        waterUsageLog2 = getWaterUsageLogSample2();
        assertThat(waterUsageLog1).isNotEqualTo(waterUsageLog2);
    }
}
