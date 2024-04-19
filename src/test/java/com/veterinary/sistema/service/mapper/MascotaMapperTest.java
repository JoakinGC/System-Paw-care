package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.MascotaAsserts.*;
import static com.veterinary.sistema.domain.MascotaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MascotaMapperTest {

    private MascotaMapper mascotaMapper;

    @BeforeEach
    void setUp() {
        mascotaMapper = new MascotaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMascotaSample1();
        var actual = mascotaMapper.toEntity(mascotaMapper.toDto(expected));
        assertMascotaAllPropertiesEquals(expected, actual);
    }
}
