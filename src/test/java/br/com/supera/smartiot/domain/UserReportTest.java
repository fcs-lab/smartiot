package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReport.class);
        UserReport userReport1 = getUserReportSample1();
        UserReport userReport2 = new UserReport();
        assertThat(userReport1).isNotEqualTo(userReport2);

        userReport2.setId(userReport1.getId());
        assertThat(userReport1).isEqualTo(userReport2);

        userReport2 = getUserReportSample2();
        assertThat(userReport1).isNotEqualTo(userReport2);
    }
}
