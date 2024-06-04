package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.DeviceTelemetryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceTelemetryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceTelemetry.class);
        DeviceTelemetry deviceTelemetry1 = getDeviceTelemetrySample1();
        DeviceTelemetry deviceTelemetry2 = new DeviceTelemetry();
        assertThat(deviceTelemetry1).isNotEqualTo(deviceTelemetry2);

        deviceTelemetry2.setId(deviceTelemetry1.getId());
        assertThat(deviceTelemetry1).isEqualTo(deviceTelemetry2);

        deviceTelemetry2 = getDeviceTelemetrySample2();
        assertThat(deviceTelemetry1).isNotEqualTo(deviceTelemetry2);
    }
}
