package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.WaterMeasurementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WaterMeasurementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterMeasurement.class);
        WaterMeasurement waterMeasurement1 = getWaterMeasurementSample1();
        WaterMeasurement waterMeasurement2 = new WaterMeasurement();
        assertThat(waterMeasurement1).isNotEqualTo(waterMeasurement2);

        waterMeasurement2.setId(waterMeasurement1.getId());
        assertThat(waterMeasurement1).isEqualTo(waterMeasurement2);

        waterMeasurement2 = getWaterMeasurementSample2();
        assertThat(waterMeasurement1).isNotEqualTo(waterMeasurement2);
    }
}
