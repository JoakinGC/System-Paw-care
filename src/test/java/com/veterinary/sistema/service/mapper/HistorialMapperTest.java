package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.HistorialAsserts.*;
import static com.veterinary.sistema.domain.HistorialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistorialMapperTest {

    private HistorialMapper historialMapper;

    @BeforeEach
    void setUp() {
        historialMapper = new HistorialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistorialSample1();
        var actual = historialMapper.toEntity(historialMapper.toDto(expected));
        assertHistorialAllPropertiesEquals(expected, actual);
    }
}
