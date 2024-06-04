package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.UserContractAsserts.*;
import static br.com.supera.smartiot.domain.UserContractTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserContractMapperTest {

    private UserContractMapper userContractMapper;

    @BeforeEach
    void setUp() {
        userContractMapper = new UserContractMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserContractSample1();
        var actual = userContractMapper.toEntity(userContractMapper.toDto(expected));
        assertUserContractAllPropertiesEquals(expected, actual);
    }
}
