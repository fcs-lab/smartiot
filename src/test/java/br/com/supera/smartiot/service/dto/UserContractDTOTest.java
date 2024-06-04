package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserContractDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserContractDTO.class);
        UserContractDTO userContractDTO1 = new UserContractDTO();
        userContractDTO1.setId(1L);
        UserContractDTO userContractDTO2 = new UserContractDTO();
        assertThat(userContractDTO1).isNotEqualTo(userContractDTO2);
        userContractDTO2.setId(userContractDTO1.getId());
        assertThat(userContractDTO1).isEqualTo(userContractDTO2);
        userContractDTO2.setId(2L);
        assertThat(userContractDTO1).isNotEqualTo(userContractDTO2);
        userContractDTO1.setId(null);
        assertThat(userContractDTO1).isNotEqualTo(userContractDTO2);
    }
}
