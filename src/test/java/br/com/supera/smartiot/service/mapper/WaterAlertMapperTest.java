package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.WaterAlertAsserts.*;
import static br.com.supera.smartiot.domain.WaterAlertTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaterAlertMapperTest {

    private WaterAlertMapper waterAlertMapper;

    @BeforeEach
    void setUp() {
        waterAlertMapper = new WaterAlertMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWaterAlertSample1();
        var actual = waterAlertMapper.toEntity(waterAlertMapper.toDto(expected));
        assertWaterAlertAllPropertiesEquals(expected, actual);
    }
}
