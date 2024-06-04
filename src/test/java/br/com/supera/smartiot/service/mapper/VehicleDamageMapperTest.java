package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.VehicleDamageAsserts.*;
import static br.com.supera.smartiot.domain.VehicleDamageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleDamageMapperTest {

    private VehicleDamageMapper vehicleDamageMapper;

    @BeforeEach
    void setUp() {
        vehicleDamageMapper = new VehicleDamageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleDamageSample1();
        var actual = vehicleDamageMapper.toEntity(vehicleDamageMapper.toDto(expected));
        assertVehicleDamageAllPropertiesEquals(expected, actual);
    }
}
