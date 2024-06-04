package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.WaterUsageLogAsserts.*;
import static br.com.supera.smartiot.domain.WaterUsageLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaterUsageLogMapperTest {

    private WaterUsageLogMapper waterUsageLogMapper;

    @BeforeEach
    void setUp() {
        waterUsageLogMapper = new WaterUsageLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWaterUsageLogSample1();
        var actual = waterUsageLogMapper.toEntity(waterUsageLogMapper.toDto(expected));
        assertWaterUsageLogAllPropertiesEquals(expected, actual);
    }
}
