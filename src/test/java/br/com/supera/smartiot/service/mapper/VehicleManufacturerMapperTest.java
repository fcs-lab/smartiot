package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.VehicleManufacturerAsserts.*;
import static br.com.supera.smartiot.domain.VehicleManufacturerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleManufacturerMapperTest {

    private VehicleManufacturerMapper vehicleManufacturerMapper;

    @BeforeEach
    void setUp() {
        vehicleManufacturerMapper = new VehicleManufacturerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleManufacturerSample1();
        var actual = vehicleManufacturerMapper.toEntity(vehicleManufacturerMapper.toDto(expected));
        assertVehicleManufacturerAllPropertiesEquals(expected, actual);
    }
}
