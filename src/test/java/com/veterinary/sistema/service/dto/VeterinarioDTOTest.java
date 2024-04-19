package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VeterinarioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VeterinarioDTO.class);
        VeterinarioDTO veterinarioDTO1 = new VeterinarioDTO();
        veterinarioDTO1.setId(1L);
        VeterinarioDTO veterinarioDTO2 = new VeterinarioDTO();
        assertThat(veterinarioDTO1).isNotEqualTo(veterinarioDTO2);
        veterinarioDTO2.setId(veterinarioDTO1.getId());
        assertThat(veterinarioDTO1).isEqualTo(veterinarioDTO2);
        veterinarioDTO2.setId(2L);
        assertThat(veterinarioDTO1).isNotEqualTo(veterinarioDTO2);
        veterinarioDTO1.setId(null);
        assertThat(veterinarioDTO1).isNotEqualTo(veterinarioDTO2);
    }
}
