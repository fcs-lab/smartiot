package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppDeviceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppDeviceDTO.class);
        AppDeviceDTO appDeviceDTO1 = new AppDeviceDTO();
        appDeviceDTO1.setId(1L);
        AppDeviceDTO appDeviceDTO2 = new AppDeviceDTO();
        assertThat(appDeviceDTO1).isNotEqualTo(appDeviceDTO2);
        appDeviceDTO2.setId(appDeviceDTO1.getId());
        assertThat(appDeviceDTO1).isEqualTo(appDeviceDTO2);
        appDeviceDTO2.setId(2L);
        assertThat(appDeviceDTO1).isNotEqualTo(appDeviceDTO2);
        appDeviceDTO1.setId(null);
        assertThat(appDeviceDTO1).isNotEqualTo(appDeviceDTO2);
    }
}
