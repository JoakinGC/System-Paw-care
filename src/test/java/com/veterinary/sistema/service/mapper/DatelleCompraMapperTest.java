package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.DatelleCompraAsserts.*;
import static com.veterinary.sistema.domain.DatelleCompraTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatelleCompraMapperTest {

    private DatelleCompraMapper datelleCompraMapper;

    @BeforeEach
    void setUp() {
        datelleCompraMapper = new DatelleCompraMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDatelleCompraSample1();
        var actual = datelleCompraMapper.toEntity(datelleCompraMapper.toDto(expected));
        assertDatelleCompraAllPropertiesEquals(expected, actual);
    }
}
