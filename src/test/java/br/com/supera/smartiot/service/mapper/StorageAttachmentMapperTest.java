package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.StorageAttachmentAsserts.*;
import static br.com.supera.smartiot.domain.StorageAttachmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageAttachmentMapperTest {

    private StorageAttachmentMapper storageAttachmentMapper;

    @BeforeEach
    void setUp() {
        storageAttachmentMapper = new StorageAttachmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStorageAttachmentSample1();
        var actual = storageAttachmentMapper.toEntity(storageAttachmentMapper.toDto(expected));
        assertStorageAttachmentAllPropertiesEquals(expected, actual);
    }
}
