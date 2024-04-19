package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.TerapiaAsserts.*;
import static com.veterinary.sistema.domain.TerapiaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TerapiaMapperTest {

    private TerapiaMapper terapiaMapper;

    @BeforeEach
    void setUp() {
        terapiaMapper = new TerapiaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTerapiaSample1();
        var actual = terapiaMapper.toEntity(terapiaMapper.toDto(expected));
        assertTerapiaAllPropertiesEquals(expected, actual);
    }
}
