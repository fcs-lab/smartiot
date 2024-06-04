package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.AppDeviceAsserts.*;
import static br.com.supera.smartiot.domain.AppDeviceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppDeviceMapperTest {

    private AppDeviceMapper appDeviceMapper;

    @BeforeEach
    void setUp() {
        appDeviceMapper = new AppDeviceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppDeviceSample1();
        var actual = appDeviceMapper.toEntity(appDeviceMapper.toDto(expected));
        assertAppDeviceAllPropertiesEquals(expected, actual);
    }
}
