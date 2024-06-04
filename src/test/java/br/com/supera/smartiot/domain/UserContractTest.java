package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ApplicationUserTestSamples.*;
import static br.com.supera.smartiot.domain.UserContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserContract.class);
        UserContract userContract1 = getUserContractSample1();
        UserContract userContract2 = new UserContract();
        assertThat(userContract1).isNotEqualTo(userContract2);

        userContract2.setId(userContract1.getId());
        assertThat(userContract1).isEqualTo(userContract2);

        userContract2 = getUserContractSample2();
        assertThat(userContract1).isNotEqualTo(userContract2);
    }

    @Test
    void userTest() {
        UserContract userContract = getUserContractRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        userContract.addUser(applicationUserBack);
        assertThat(userContract.getUsers()).containsOnly(applicationUserBack);

        userContract.removeUser(applicationUserBack);
        assertThat(userContract.getUsers()).doesNotContain(applicationUserBack);

        userContract.users(new HashSet<>(Set.of(applicationUserBack)));
        assertThat(userContract.getUsers()).containsOnly(applicationUserBack);

        userContract.setUsers(new HashSet<>());
        assertThat(userContract.getUsers()).doesNotContain(applicationUserBack);
    }
}
