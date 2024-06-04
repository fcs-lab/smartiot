package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.UserReportAsserts.*;
import static br.com.supera.smartiot.domain.UserReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserReportMapperTest {

    private UserReportMapper userReportMapper;

    @BeforeEach
    void setUp() {
        userReportMapper = new UserReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserReportSample1();
        var actual = userReportMapper.toEntity(userReportMapper.toDto(expected));
        assertUserReportAllPropertiesEquals(expected, actual);
    }
}
