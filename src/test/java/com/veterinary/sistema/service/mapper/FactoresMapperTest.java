package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.FactoresAsserts.*;
import static com.veterinary.sistema.domain.FactoresTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactoresMapperTest {

    private FactoresMapper factoresMapper;

    @BeforeEach
    void setUp() {
        factoresMapper = new FactoresMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFactoresSample1();
        var actual = factoresMapper.toEntity(factoresMapper.toDto(expected));
        assertFactoresAllPropertiesEquals(expected, actual);
    }
}
