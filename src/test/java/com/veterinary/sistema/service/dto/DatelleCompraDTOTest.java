package com.veterinary.sistema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DatelleCompraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatelleCompraDTO.class);
        DatelleCompraDTO datelleCompraDTO1 = new DatelleCompraDTO();
        datelleCompraDTO1.setId(1L);
        DatelleCompraDTO datelleCompraDTO2 = new DatelleCompraDTO();
        assertThat(datelleCompraDTO1).isNotEqualTo(datelleCompraDTO2);
        datelleCompraDTO2.setId(datelleCompraDTO1.getId());
        assertThat(datelleCompraDTO1).isEqualTo(datelleCompraDTO2);
        datelleCompraDTO2.setId(2L);
        assertThat(datelleCompraDTO1).isNotEqualTo(datelleCompraDTO2);
        datelleCompraDTO1.setId(null);
        assertThat(datelleCompraDTO1).isNotEqualTo(datelleCompraDTO2);
    }
}
