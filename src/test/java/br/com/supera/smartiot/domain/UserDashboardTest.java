package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.UserDashboardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserDashboardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDashboard.class);
        UserDashboard userDashboard1 = getUserDashboardSample1();
        UserDashboard userDashboard2 = new UserDashboard();
        assertThat(userDashboard1).isNotEqualTo(userDashboard2);

        userDashboard2.setId(userDashboard1.getId());
        assertThat(userDashboard1).isEqualTo(userDashboard2);

        userDashboard2 = getUserDashboardSample2();
        assertThat(userDashboard1).isNotEqualTo(userDashboard2);
    }
}
