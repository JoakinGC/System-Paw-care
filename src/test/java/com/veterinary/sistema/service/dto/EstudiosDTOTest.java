package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstudiosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstudiosDTO.class);
        EstudiosDTO estudiosDTO1 = new EstudiosDTO();
        estudiosDTO1.setId(1L);
        EstudiosDTO estudiosDTO2 = new EstudiosDTO();
        assertThat(estudiosDTO1).isNotEqualTo(estudiosDTO2);
        estudiosDTO2.setId(estudiosDTO1.getId());
        assertThat(estudiosDTO1).isEqualTo(estudiosDTO2);
        estudiosDTO2.setId(2L);
        assertThat(estudiosDTO1).isNotEqualTo(estudiosDTO2);
        estudiosDTO1.setId(null);
        assertThat(estudiosDTO1).isNotEqualTo(estudiosDTO2);
    }
}
