package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.VeterinarioAsserts.*;
import static com.veterinary.sistema.domain.VeterinarioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VeterinarioMapperTest {

    private VeterinarioMapper veterinarioMapper;

    @BeforeEach
    void setUp() {
        veterinarioMapper = new VeterinarioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVeterinarioSample1();
        var actual = veterinarioMapper.toEntity(veterinarioMapper.toDto(expected));
        assertVeterinarioAllPropertiesEquals(expected, actual);
    }
}
