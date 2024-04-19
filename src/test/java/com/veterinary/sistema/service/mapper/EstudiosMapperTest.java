package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.EstudiosAsserts.*;
import static com.veterinary.sistema.domain.EstudiosTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstudiosMapperTest {

    private EstudiosMapper estudiosMapper;

    @BeforeEach
    void setUp() {
        estudiosMapper = new EstudiosMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEstudiosSample1();
        var actual = estudiosMapper.toEntity(estudiosMapper.toDto(expected));
        assertEstudiosAllPropertiesEquals(expected, actual);
    }
}
