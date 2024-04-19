package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.MedicamentoAsserts.*;
import static com.veterinary.sistema.domain.MedicamentoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicamentoMapperTest {

    private MedicamentoMapper medicamentoMapper;

    @BeforeEach
    void setUp() {
        medicamentoMapper = new MedicamentoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicamentoSample1();
        var actual = medicamentoMapper.toEntity(medicamentoMapper.toDto(expected));
        assertMedicamentoAllPropertiesEquals(expected, actual);
    }
}
