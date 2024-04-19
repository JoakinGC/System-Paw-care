package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DuenoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DuenoDTO.class);
        DuenoDTO duenoDTO1 = new DuenoDTO();
        duenoDTO1.setId(1L);
        DuenoDTO duenoDTO2 = new DuenoDTO();
        assertThat(duenoDTO1).isNotEqualTo(duenoDTO2);
        duenoDTO2.setId(duenoDTO1.getId());
        assertThat(duenoDTO1).isEqualTo(duenoDTO2);
        duenoDTO2.setId(2L);
        assertThat(duenoDTO1).isNotEqualTo(duenoDTO2);
        duenoDTO1.setId(null);
        assertThat(duenoDTO1).isNotEqualTo(duenoDTO2);
    }
}
