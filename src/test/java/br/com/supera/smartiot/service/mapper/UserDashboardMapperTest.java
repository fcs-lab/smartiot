package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.UserDashboardAsserts.*;
import static br.com.supera.smartiot.domain.UserDashboardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDashboardMapperTest {

    private UserDashboardMapper userDashboardMapper;

    @BeforeEach
    void setUp() {
        userDashboardMapper = new UserDashboardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserDashboardSample1();
        var actual = userDashboardMapper.toEntity(userDashboardMapper.toDto(expected));
        assertUserDashboardAllPropertiesEquals(expected, actual);
    }
}
