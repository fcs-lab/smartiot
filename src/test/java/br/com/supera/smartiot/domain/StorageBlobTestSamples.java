package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StorageBlobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StorageBlob getStorageBlobSample1() {
        return new StorageBlob()
            .id(1L)
            .fileName("fileName1")
            .contentType("contentType1")
            .byteSize(1L)
            .checksum("checksum1")
            .lastModifiedBy("lastModifiedBy1")
            .key("key1")
            .metadata("metadata1");
    }

    public static StorageBlob getStorageBlobSample2() {
        return new StorageBlob()
            .id(2L)
            .fileName("fileName2")
            .contentType("contentType2")
            .byteSize(2L)
            .checksum("checksum2")
            .lastModifiedBy("lastModifiedBy2")
            .key("key2")
            .metadata("metadata2");
    }

    public static StorageBlob getStorageBlobRandomSampleGenerator() {
        return new StorageBlob()
            .id(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .contentType(UUID.randomUUID().toString())
            .byteSize(longCount.incrementAndGet())
            .checksum(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .key(UUID.randomUUID().toString())
            .metadata(UUID.randomUUID().toString());
    }
}
