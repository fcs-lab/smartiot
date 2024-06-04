package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ApplicationUserTestSamples.*;
import static br.com.supera.smartiot.domain.UserAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAccount.class);
        UserAccount userAccount1 = getUserAccountSample1();
        UserAccount userAccount2 = new UserAccount();
        assertThat(userAccount1).isNotEqualTo(userAccount2);

        userAccount2.setId(userAccount1.getId());
        assertThat(userAccount1).isEqualTo(userAccount2);

        userAccount2 = getUserAccountSample2();
        assertThat(userAccount1).isNotEqualTo(userAccount2);
    }

    @Test
    void applicationUserTest() {
        UserAccount userAccount = getUserAccountRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        userAccount.setApplicationUser(applicationUserBack);
        assertThat(userAccount.getApplicationUser()).isEqualTo(applicationUserBack);

        userAccount.applicationUser(null);
        assertThat(userAccount.getApplicationUser()).isNull();
    }
}
