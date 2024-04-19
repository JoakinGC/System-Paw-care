package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EsteticaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EsteticaDTO.class);
        EsteticaDTO esteticaDTO1 = new EsteticaDTO();
        esteticaDTO1.setId(1L);
        EsteticaDTO esteticaDTO2 = new EsteticaDTO();
        assertThat(esteticaDTO1).isNotEqualTo(esteticaDTO2);
        esteticaDTO2.setId(esteticaDTO1.getId());
        assertThat(esteticaDTO1).isEqualTo(esteticaDTO2);
        esteticaDTO2.setId(2L);
        assertThat(esteticaDTO1).isNotEqualTo(esteticaDTO2);
        esteticaDTO1.setId(null);
        assertThat(esteticaDTO1).isNotEqualTo(esteticaDTO2);
    }
}
