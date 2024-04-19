package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.EnfermedadAsserts.*;
import static com.veterinary.sistema.domain.EnfermedadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnfermedadMapperTest {

    private EnfermedadMapper enfermedadMapper;

    @BeforeEach
    void setUp() {
        enfermedadMapper = new EnfermedadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnfermedadSample1();
        var actual = enfermedadMapper.toEntity(enfermedadMapper.toDto(expected));
        assertEnfermedadAllPropertiesEquals(expected, actual);
    }
}
