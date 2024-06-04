package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemAlertDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemAlertDTO.class);
        SystemAlertDTO systemAlertDTO1 = new SystemAlertDTO();
        systemAlertDTO1.setId(1L);
        SystemAlertDTO systemAlertDTO2 = new SystemAlertDTO();
        assertThat(systemAlertDTO1).isNotEqualTo(systemAlertDTO2);
        systemAlertDTO2.setId(systemAlertDTO1.getId());
        assertThat(systemAlertDTO1).isEqualTo(systemAlertDTO2);
        systemAlertDTO2.setId(2L);
        assertThat(systemAlertDTO1).isNotEqualTo(systemAlertDTO2);
        systemAlertDTO1.setId(null);
        assertThat(systemAlertDTO1).isNotEqualTo(systemAlertDTO2);
    }
}
