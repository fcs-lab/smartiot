package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceCommandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceCommandDTO.class);
        DeviceCommandDTO deviceCommandDTO1 = new DeviceCommandDTO();
        deviceCommandDTO1.setId(1L);
        DeviceCommandDTO deviceCommandDTO2 = new DeviceCommandDTO();
        assertThat(deviceCommandDTO1).isNotEqualTo(deviceCommandDTO2);
        deviceCommandDTO2.setId(deviceCommandDTO1.getId());
        assertThat(deviceCommandDTO1).isEqualTo(deviceCommandDTO2);
        deviceCommandDTO2.setId(2L);
        assertThat(deviceCommandDTO1).isNotEqualTo(deviceCommandDTO2);
        deviceCommandDTO1.setId(null);
        assertThat(deviceCommandDTO1).isNotEqualTo(deviceCommandDTO2);
    }
}
