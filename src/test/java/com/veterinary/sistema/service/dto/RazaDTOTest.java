package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RazaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RazaDTO.class);
        RazaDTO razaDTO1 = new RazaDTO();
        razaDTO1.setId(1L);
        RazaDTO razaDTO2 = new RazaDTO();
        assertThat(razaDTO1).isNotEqualTo(razaDTO2);
        razaDTO2.setId(razaDTO1.getId());
        assertThat(razaDTO1).isEqualTo(razaDTO2);
        razaDTO2.setId(2L);
        assertThat(razaDTO1).isNotEqualTo(razaDTO2);
        razaDTO1.setId(null);
        assertThat(razaDTO1).isNotEqualTo(razaDTO2);
    }
}
