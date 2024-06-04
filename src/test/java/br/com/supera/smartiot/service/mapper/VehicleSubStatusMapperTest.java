package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.VehicleSubStatusAsserts.*;
import static br.com.supera.smartiot.domain.VehicleSubStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleSubStatusMapperTest {

    private VehicleSubStatusMapper vehicleSubStatusMapper;

    @BeforeEach
    void setUp() {
        vehicleSubStatusMapper = new VehicleSubStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleSubStatusSample1();
        var actual = vehicleSubStatusMapper.toEntity(vehicleSubStatusMapper.toDto(expected));
        assertVehicleSubStatusAllPropertiesEquals(expected, actual);
    }
}
