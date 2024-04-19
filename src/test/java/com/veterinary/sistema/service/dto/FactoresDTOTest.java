package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactoresDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactoresDTO.class);
        FactoresDTO factoresDTO1 = new FactoresDTO();
        factoresDTO1.setId(1L);
        FactoresDTO factoresDTO2 = new FactoresDTO();
        assertThat(factoresDTO1).isNotEqualTo(factoresDTO2);
        factoresDTO2.setId(factoresDTO1.getId());
        assertThat(factoresDTO1).isEqualTo(factoresDTO2);
        factoresDTO2.setId(2L);
        assertThat(factoresDTO1).isNotEqualTo(factoresDTO2);
        factoresDTO1.setId(null);
        assertThat(factoresDTO1).isNotEqualTo(factoresDTO2);
    }
}
