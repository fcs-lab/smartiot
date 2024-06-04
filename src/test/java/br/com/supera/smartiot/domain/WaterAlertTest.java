package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.WaterAlertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaterAlertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterAlert.class);
        WaterAlert waterAlert1 = getWaterAlertSample1();
        WaterAlert waterAlert2 = new WaterAlert();
        assertThat(waterAlert1).isNotEqualTo(waterAlert2);

        waterAlert2.setId(waterAlert1.getId());
        assertThat(waterAlert1).isEqualTo(waterAlert2);

        waterAlert2 = getWaterAlertSample2();
        assertThat(waterAlert1).isNotEqualTo(waterAlert2);
    }
}
