package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.UserRoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRole.class);
        UserRole userRole1 = getUserRoleSample1();
        UserRole userRole2 = new UserRole();
        assertThat(userRole1).isNotEqualTo(userRole2);

        userRole2.setId(userRole1.getId());
        assertThat(userRole1).isEqualTo(userRole2);

        userRole2 = getUserRoleSample2();
        assertThat(userRole1).isNotEqualTo(userRole2);
    }
}
