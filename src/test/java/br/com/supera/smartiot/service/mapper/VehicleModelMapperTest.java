package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.VehicleModelAsserts.*;
import static br.com.supera.smartiot.domain.VehicleModelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleModelMapperTest {

    private VehicleModelMapper vehicleModelMapper;

    @BeforeEach
    void setUp() {
        vehicleModelMapper = new VehicleModelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleModelSample1();
        var actual = vehicleModelMapper.toEntity(vehicleModelMapper.toDto(expected));
        assertVehicleModelAllPropertiesEquals(expected, actual);
    }
}
