package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.VehicleInfoAsserts.*;
import static br.com.supera.smartiot.domain.VehicleInfoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleInfoMapperTest {

    private VehicleInfoMapper vehicleInfoMapper;

    @BeforeEach
    void setUp() {
        vehicleInfoMapper = new VehicleInfoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleInfoSample1();
        var actual = vehicleInfoMapper.toEntity(vehicleInfoMapper.toDto(expected));
        assertVehicleInfoAllPropertiesEquals(expected, actual);
    }
}
