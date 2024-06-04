package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageAttachmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageAttachmentDTO.class);
        StorageAttachmentDTO storageAttachmentDTO1 = new StorageAttachmentDTO();
        storageAttachmentDTO1.setId(1L);
        StorageAttachmentDTO storageAttachmentDTO2 = new StorageAttachmentDTO();
        assertThat(storageAttachmentDTO1).isNotEqualTo(storageAttachmentDTO2);
        storageAttachmentDTO2.setId(storageAttachmentDTO1.getId());
        assertThat(storageAttachmentDTO1).isEqualTo(storageAttachmentDTO2);
        storageAttachmentDTO2.setId(2L);
        assertThat(storageAttachmentDTO1).isNotEqualTo(storageAttachmentDTO2);
        storageAttachmentDTO1.setId(null);
        assertThat(storageAttachmentDTO1).isNotEqualTo(storageAttachmentDTO2);
    }
}
