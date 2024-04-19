package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EspecieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EspecieDTO.class);
        EspecieDTO especieDTO1 = new EspecieDTO();
        especieDTO1.setId(1L);
        EspecieDTO especieDTO2 = new EspecieDTO();
        assertThat(especieDTO1).isNotEqualTo(especieDTO2);
        especieDTO2.setId(especieDTO1.getId());
        assertThat(especieDTO1).isEqualTo(especieDTO2);
        especieDTO2.setId(2L);
        assertThat(especieDTO1).isNotEqualTo(especieDTO2);
        especieDTO1.setId(null);
        assertThat(especieDTO1).isNotEqualTo(especieDTO2);
    }
}
