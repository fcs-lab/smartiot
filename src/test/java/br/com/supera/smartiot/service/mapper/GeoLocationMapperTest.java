package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.GeoLocationAsserts.*;
import static br.com.supera.smartiot.domain.GeoLocationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeoLocationMapperTest {

    private GeoLocationMapper geoLocationMapper;

    @BeforeEach
    void setUp() {
        geoLocationMapper = new GeoLocationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGeoLocationSample1();
        var actual = geoLocationMapper.toEntity(geoLocationMapper.toDto(expected));
        assertGeoLocationAllPropertiesEquals(expected, actual);
    }
}
