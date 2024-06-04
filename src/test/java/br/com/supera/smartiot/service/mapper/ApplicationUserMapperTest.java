package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ApplicationUserAsserts.*;
import static br.com.supera.smartiot.domain.ApplicationUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationUserMapperTest {

    private ApplicationUserMapper applicationUserMapper;

    @BeforeEach
    void setUp() {
        applicationUserMapper = new ApplicationUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApplicationUserSample1();
        var actual = applicationUserMapper.toEntity(applicationUserMapper.toDto(expected));
        assertApplicationUserAllPropertiesEquals(expected, actual);
    }
}
