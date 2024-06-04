package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.UserAccountAsserts.*;
import static br.com.supera.smartiot.domain.UserAccountTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAccountMapperTest {

    private UserAccountMapper userAccountMapper;

    @BeforeEach
    void setUp() {
        userAccountMapper = new UserAccountMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserAccountSample1();
        var actual = userAccountMapper.toEntity(userAccountMapper.toDto(expected));
        assertUserAccountAllPropertiesEquals(expected, actual);
    }
}
