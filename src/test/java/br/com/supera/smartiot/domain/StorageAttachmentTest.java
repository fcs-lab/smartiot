package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.StorageAttachmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageAttachment.class);
        StorageAttachment storageAttachment1 = getStorageAttachmentSample1();
        StorageAttachment storageAttachment2 = new StorageAttachment();
        assertThat(storageAttachment1).isNotEqualTo(storageAttachment2);

        storageAttachment2.setId(storageAttachment1.getId());
        assertThat(storageAttachment1).isEqualTo(storageAttachment2);

        storageAttachment2 = getStorageAttachmentSample2();
        assertThat(storageAttachment1).isNotEqualTo(storageAttachment2);
    }
}
