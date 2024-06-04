package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.VehicleStatusLogAsserts.*;
import static br.com.supera.smartiot.domain.VehicleStatusLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleStatusLogMapperTest {

    private VehicleStatusLogMapper vehicleStatusLogMapper;

    @BeforeEach
    void setUp() {
        vehicleStatusLogMapper = new VehicleStatusLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleStatusLogSample1();
        var actual = vehicleStatusLogMapper.toEntity(vehicleStatusLogMapper.toDto(expected));
        assertVehicleStatusLogAllPropertiesEquals(expected, actual);
    }
}
