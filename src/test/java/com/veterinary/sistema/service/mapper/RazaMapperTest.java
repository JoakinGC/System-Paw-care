package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.RazaAsserts.*;
import static com.veterinary.sistema.domain.RazaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RazaMapperTest {

    private RazaMapper razaMapper;

    @BeforeEach
    void setUp() {
        razaMapper = new RazaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRazaSample1();
        var actual = razaMapper.toEntity(razaMapper.toDto(expected));
        assertRazaAllPropertiesEquals(expected, actual);
    }
}
