package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.VehicleGroupAsserts.*;
import static br.com.supera.smartiot.domain.VehicleGroupTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleGroupMapperTest {

    private VehicleGroupMapper vehicleGroupMapper;

    @BeforeEach
    void setUp() {
        vehicleGroupMapper = new VehicleGroupMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleGroupSample1();
        var actual = vehicleGroupMapper.toEntity(vehicleGroupMapper.toDto(expected));
        assertVehicleGroupAllPropertiesEquals(expected, actual);
    }
}
