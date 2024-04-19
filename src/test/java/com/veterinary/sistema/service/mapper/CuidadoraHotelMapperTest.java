package com.veterinary.sistema.service.mapper;

import static com.veterinary.sistema.domain.CuidadoraHotelAsserts.*;
import static com.veterinary.sistema.domain.CuidadoraHotelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CuidadoraHotelMapperTest {

    private CuidadoraHotelMapper cuidadoraHotelMapper;

    @BeforeEach
    void setUp() {
        cuidadoraHotelMapper = new CuidadoraHotelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCuidadoraHotelSample1();
        var actual = cuidadoraHotelMapper.toEntity(cuidadoraHotelMapper.toDto(expected));
        assertCuidadoraHotelAllPropertiesEquals(expected, actual);
    }
}
