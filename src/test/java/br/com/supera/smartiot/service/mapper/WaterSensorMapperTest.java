package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.WaterSensorAsserts.*;
import static br.com.supera.smartiot.domain.WaterSensorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaterSensorMapperTest {

    private WaterSensorMapper waterSensorMapper;

    @BeforeEach
    void setUp() {
        waterSensorMapper = new WaterSensorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWaterSensorSample1();
        var actual = waterSensorMapper.toEntity(waterSensorMapper.toDto(expected));
        assertWaterSensorAllPropertiesEquals(expected, actual);
    }
}
