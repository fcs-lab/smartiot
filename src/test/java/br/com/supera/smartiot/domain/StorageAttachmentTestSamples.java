package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StorageAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StorageAttachment getStorageAttachmentSample1() {
        return new StorageAttachment()
            .id(1L)
            .attachmentName("attachmentName1")
            .recordType("recordType1")
            .recordId(1L)
            .lastModifiedBy("lastModifiedBy1")
            .blobId(1L);
    }

    public static StorageAttachment getStorageAttachmentSample2() {
        return new StorageAttachment()
            .id(2L)
            .attachmentName("attachmentName2")
            .recordType("recordType2")
            .recordId(2L)
            .lastModifiedBy("lastModifiedBy2")
            .blobId(2L);
    }

    public static StorageAttachment getStorageAttachmentRandomSampleGenerator() {
        return new StorageAttachment()
            .id(longCount.incrementAndGet())
            .attachmentName(UUID.randomUUID().toString())
            .recordType(UUID.randomUUID().toString())
            .recordId(longCount.incrementAndGet())
            .lastModifiedBy(UUID.randomUUID().toString())
            .blobId(longCount.incrementAndGet());
    }
}
