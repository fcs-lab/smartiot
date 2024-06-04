package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.StorageBlobAsserts.*;
import static br.com.supera.smartiot.domain.StorageBlobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageBlobMapperTest {

    private StorageBlobMapper storageBlobMapper;

    @BeforeEach
    void setUp() {
        storageBlobMapper = new StorageBlobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStorageBlobSample1();
        var actual = storageBlobMapper.toEntity(storageBlobMapper.toDto(expected));
        assertStorageBlobAllPropertiesEquals(expected, actual);
    }
}
