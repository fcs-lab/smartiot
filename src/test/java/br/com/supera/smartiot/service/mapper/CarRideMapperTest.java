package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.CarRideAsserts.*;
import static br.com.supera.smartiot.domain.CarRideTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarRideMapperTest {

    private CarRideMapper carRideMapper;

    @BeforeEach
    void setUp() {
        carRideMapper = new CarRideMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCarRideSample1();
        var actual = carRideMapper.toEntity(carRideMapper.toDto(expected));
        assertCarRideAllPropertiesEquals(expected, actual);
    }
}
