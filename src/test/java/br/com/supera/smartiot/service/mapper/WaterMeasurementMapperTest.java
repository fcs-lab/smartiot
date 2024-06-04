package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.WaterMeasurementAsserts.*;
import static br.com.supera.smartiot.domain.WaterMeasurementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaterMeasurementMapperTest {

    private WaterMeasurementMapper waterMeasurementMapper;

    @BeforeEach
    void setUp() {
        waterMeasurementMapper = new WaterMeasurementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWaterMeasurementSample1();
        var actual = waterMeasurementMapper.toEntity(waterMeasurementMapper.toDto(expected));
        assertWaterMeasurementAllPropertiesEquals(expected, actual);
    }
}
