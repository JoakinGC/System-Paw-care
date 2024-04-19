package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.TratamientoAsserts.*;
import static com.veterinary.sistema.domain.TratamientoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TratamientoMapperTest {

    private TratamientoMapper tratamientoMapper;

    @BeforeEach
    void setUp() {
        tratamientoMapper = new TratamientoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTratamientoSample1();
        var actual = tratamientoMapper.toEntity(tratamientoMapper.toDto(expected));
        assertTratamientoAllPropertiesEquals(expected, actual);
    }
}
