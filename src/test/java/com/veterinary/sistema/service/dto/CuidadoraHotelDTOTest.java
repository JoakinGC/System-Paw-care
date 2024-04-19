package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CuidadoraHotelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuidadoraHotelDTO.class);
        CuidadoraHotelDTO cuidadoraHotelDTO1 = new CuidadoraHotelDTO();
        cuidadoraHotelDTO1.setId(1L);
        CuidadoraHotelDTO cuidadoraHotelDTO2 = new CuidadoraHotelDTO();
        assertThat(cuidadoraHotelDTO1).isNotEqualTo(cuidadoraHotelDTO2);
        cuidadoraHotelDTO2.setId(cuidadoraHotelDTO1.getId());
        assertThat(cuidadoraHotelDTO1).isEqualTo(cuidadoraHotelDTO2);
        cuidadoraHotelDTO2.setId(2L);
        assertThat(cuidadoraHotelDTO1).isNotEqualTo(cuidadoraHotelDTO2);
        cuidadoraHotelDTO1.setId(null);
        assertThat(cuidadoraHotelDTO1).isNotEqualTo(cuidadoraHotelDTO2);
    }
}
