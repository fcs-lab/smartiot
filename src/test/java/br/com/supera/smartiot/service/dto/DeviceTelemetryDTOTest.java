package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceTelemetryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceTelemetryDTO.class);
        DeviceTelemetryDTO deviceTelemetryDTO1 = new DeviceTelemetryDTO();
        deviceTelemetryDTO1.setId(1L);
        DeviceTelemetryDTO deviceTelemetryDTO2 = new DeviceTelemetryDTO();
        assertThat(deviceTelemetryDTO1).isNotEqualTo(deviceTelemetryDTO2);
        deviceTelemetryDTO2.setId(deviceTelemetryDTO1.getId());
        assertThat(deviceTelemetryDTO1).isEqualTo(deviceTelemetryDTO2);
        deviceTelemetryDTO2.setId(2L);
        assertThat(deviceTelemetryDTO1).isNotEqualTo(deviceTelemetryDTO2);
        deviceTelemetryDTO1.setId(null);
        assertThat(deviceTelemetryDTO1).isNotEqualTo(deviceTelemetryDTO2);
    }
}
