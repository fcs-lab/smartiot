package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserDashboardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDashboardDTO.class);
        UserDashboardDTO userDashboardDTO1 = new UserDashboardDTO();
        userDashboardDTO1.setId(1L);
        UserDashboardDTO userDashboardDTO2 = new UserDashboardDTO();
        assertThat(userDashboardDTO1).isNotEqualTo(userDashboardDTO2);
        userDashboardDTO2.setId(userDashboardDTO1.getId());
        assertThat(userDashboardDTO1).isEqualTo(userDashboardDTO2);
        userDashboardDTO2.setId(2L);
        assertThat(userDashboardDTO1).isNotEqualTo(userDashboardDTO2);
        userDashboardDTO1.setId(null);
        assertThat(userDashboardDTO1).isNotEqualTo(userDashboardDTO2);
    }
}
