package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.DuenoAsserts.*;
import static com.veterinary.sistema.domain.DuenoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DuenoMapperTest {

    private DuenoMapper duenoMapper;

    @BeforeEach
    void setUp() {
        duenoMapper = new DuenoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDuenoSample1();
        var actual = duenoMapper.toEntity(duenoMapper.toDto(expected));
        assertDuenoAllPropertiesEquals(expected, actual);
    }
}
