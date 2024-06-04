package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ApplicationUserTestSamples.*;
import static br.com.supera.smartiot.domain.UserAccountTestSamples.*;
import static br.com.supera.smartiot.domain.UserContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void userAccountTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        UserAccount userAccountBack = getUserAccountRandomSampleGenerator();

        applicationUser.setUserAccount(userAccountBack);
        assertThat(applicationUser.getUserAccount()).isEqualTo(userAccountBack);
        assertThat(userAccountBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.userAccount(null);
        assertThat(applicationUser.getUserAccount()).isNull();
        assertThat(userAccountBack.getApplicationUser()).isNull();
    }

    @Test
    void contractsTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        UserContract userContractBack = getUserContractRandomSampleGenerator();

        applicationUser.addContracts(userContractBack);
        assertThat(applicationUser.getContracts()).containsOnly(userContractBack);
        assertThat(userContractBack.getUsers()).containsOnly(applicationUser);

        applicationUser.removeContracts(userContractBack);
        assertThat(applicationUser.getContracts()).doesNotContain(userContractBack);
        assertThat(userContractBack.getUsers()).doesNotContain(applicationUser);

        applicationUser.contracts(new HashSet<>(Set.of(userContractBack)));
        assertThat(applicationUser.getContracts()).containsOnly(userContractBack);
        assertThat(userContractBack.getUsers()).containsOnly(applicationUser);

        applicationUser.setContracts(new HashSet<>());
        assertThat(applicationUser.getContracts()).doesNotContain(userContractBack);
        assertThat(userContractBack.getUsers()).doesNotContain(applicationUser);
    }
}
