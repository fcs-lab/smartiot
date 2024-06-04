package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageBlobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageBlobDTO.class);
        StorageBlobDTO storageBlobDTO1 = new StorageBlobDTO();
        storageBlobDTO1.setId(1L);
        StorageBlobDTO storageBlobDTO2 = new StorageBlobDTO();
        assertThat(storageBlobDTO1).isNotEqualTo(storageBlobDTO2);
        storageBlobDTO2.setId(storageBlobDTO1.getId());
        assertThat(storageBlobDTO1).isEqualTo(storageBlobDTO2);
        storageBlobDTO2.setId(2L);
        assertThat(storageBlobDTO1).isNotEqualTo(storageBlobDTO2);
        storageBlobDTO1.setId(null);
        assertThat(storageBlobDTO1).isNotEqualTo(storageBlobDTO2);
    }
}
