package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.EsteticaAsserts.*;
import static com.veterinary.sistema.domain.EsteticaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EsteticaMapperTest {

    private EsteticaMapper esteticaMapper;

    @BeforeEach
    void setUp() {
        esteticaMapper = new EsteticaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEsteticaSample1();
        var actual = esteticaMapper.toEntity(esteticaMapper.toDto(expected));
        assertEsteticaAllPropertiesEquals(expected, actual);
    }
}
