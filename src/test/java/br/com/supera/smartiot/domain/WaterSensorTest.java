package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.WaterSensorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaterSensorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterSensor.class);
        WaterSensor waterSensor1 = getWaterSensorSample1();
        WaterSensor waterSensor2 = new WaterSensor();
        assertThat(waterSensor1).isNotEqualTo(waterSensor2);

        waterSensor2.setId(waterSensor1.getId());
        assertThat(waterSensor1).isEqualTo(waterSensor2);

        waterSensor2 = getWaterSensorSample2();
        assertThat(waterSensor1).isNotEqualTo(waterSensor2);
    }
}
