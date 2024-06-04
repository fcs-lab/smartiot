package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.DeviceCommandAsserts.*;
import static br.com.supera.smartiot.domain.DeviceCommandTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceCommandMapperTest {

    private DeviceCommandMapper deviceCommandMapper;

    @BeforeEach
    void setUp() {
        deviceCommandMapper = new DeviceCommandMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDeviceCommandSample1();
        var actual = deviceCommandMapper.toEntity(deviceCommandMapper.toDto(expected));
        assertDeviceCommandAllPropertiesEquals(expected, actual);
    }
}
