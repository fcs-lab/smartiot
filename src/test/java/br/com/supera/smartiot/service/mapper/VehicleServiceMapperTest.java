package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.VehicleServiceAsserts.*;
import static br.com.supera.smartiot.domain.VehicleServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleServiceMapperTest {

    private VehicleServiceMapper vehicleServiceMapper;

    @BeforeEach
    void setUp() {
        vehicleServiceMapper = new VehicleServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleServiceSample1();
        var actual = vehicleServiceMapper.toEntity(vehicleServiceMapper.toDto(expected));
        assertVehicleServiceAllPropertiesEquals(expected, actual);
    }
}
