package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.StorageBlobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageBlobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageBlob.class);
        StorageBlob storageBlob1 = getStorageBlobSample1();
        StorageBlob storageBlob2 = new StorageBlob();
        assertThat(storageBlob1).isNotEqualTo(storageBlob2);

        storageBlob2.setId(storageBlob1.getId());
        assertThat(storageBlob1).isEqualTo(storageBlob2);

        storageBlob2 = getStorageBlobSample2();
        assertThat(storageBlob1).isNotEqualTo(storageBlob2);
    }
}
