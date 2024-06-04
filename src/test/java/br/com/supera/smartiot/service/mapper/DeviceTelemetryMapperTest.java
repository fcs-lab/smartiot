package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.DeviceTelemetryAsserts.*;
import static br.com.supera.smartiot.domain.DeviceTelemetryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceTelemetryMapperTest {

    private DeviceTelemetryMapper deviceTelemetryMapper;

    @BeforeEach
    void setUp() {
        deviceTelemetryMapper = new DeviceTelemetryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDeviceTelemetrySample1();
        var actual = deviceTelemetryMapper.toEntity(deviceTelemetryMapper.toDto(expected));
        assertDeviceTelemetryAllPropertiesEquals(expected, actual);
    }
}
