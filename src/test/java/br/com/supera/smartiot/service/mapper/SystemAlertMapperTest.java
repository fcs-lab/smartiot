package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.SystemAlertAsserts.*;
import static br.com.supera.smartiot.domain.SystemAlertTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemAlertMapperTest {

    private SystemAlertMapper systemAlertMapper;

    @BeforeEach
    void setUp() {
        systemAlertMapper = new SystemAlertMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSystemAlertSample1();
        var actual = systemAlertMapper.toEntity(systemAlertMapper.toDto(expected));
        assertSystemAlertAllPropertiesEquals(expected, actual);
    }
}
