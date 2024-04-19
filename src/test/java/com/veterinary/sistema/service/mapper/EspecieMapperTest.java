package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.EspecieAsserts.*;
import static com.veterinary.sistema.domain.EspecieTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EspecieMapperTest {

    private EspecieMapper especieMapper;

    @BeforeEach
    void setUp() {
        especieMapper = new EspecieMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEspecieSample1();
        var actual = especieMapper.toEntity(especieMapper.toDto(expected));
        assertEspecieAllPropertiesEquals(expected, actual);
    }
}
