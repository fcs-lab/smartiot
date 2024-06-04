package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.DeviceCommandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceCommandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceCommand.class);
        DeviceCommand deviceCommand1 = getDeviceCommandSample1();
        DeviceCommand deviceCommand2 = new DeviceCommand();
        assertThat(deviceCommand1).isNotEqualTo(deviceCommand2);

        deviceCommand2.setId(deviceCommand1.getId());
        assertThat(deviceCommand1).isEqualTo(deviceCommand2);

        deviceCommand2 = getDeviceCommandSample2();
        assertThat(deviceCommand1).isNotEqualTo(deviceCommand2);
    }
}
