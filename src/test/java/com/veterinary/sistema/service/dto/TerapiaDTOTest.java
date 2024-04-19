package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TerapiaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TerapiaDTO.class);
        TerapiaDTO terapiaDTO1 = new TerapiaDTO();
        terapiaDTO1.setId(1L);
        TerapiaDTO terapiaDTO2 = new TerapiaDTO();
        assertThat(terapiaDTO1).isNotEqualTo(terapiaDTO2);
        terapiaDTO2.setId(terapiaDTO1.getId());
        assertThat(terapiaDTO1).isEqualTo(terapiaDTO2);
        terapiaDTO2.setId(2L);
        assertThat(terapiaDTO1).isNotEqualTo(terapiaDTO2);
        terapiaDTO1.setId(null);
        assertThat(terapiaDTO1).isNotEqualTo(terapiaDTO2);
    }
}
