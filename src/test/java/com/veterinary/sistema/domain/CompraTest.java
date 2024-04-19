package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.CompraTestSamples.*;
import static com.veterinary.sistema.domain.DatelleCompraTestSamples.*;
import static com.veterinary.sistema.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compra.class);
        Compra compra1 = getCompraSample1();
        Compra compra2 = new Compra();
        assertThat(compra1).isNotEqualTo(compra2);

        compra2.setId(compra1.getId());
        assertThat(compra1).isEqualTo(compra2);

        compra2 = getCompraSample2();
        assertThat(compra1).isNotEqualTo(compra2);
    }

    @Test
    void datelleCompraTest() throws Exception {
        Compra compra = getCompraRandomSampleGenerator();
        DatelleCompra datelleCompraBack = getDatelleCompraRandomSampleGenerator();

        compra.addDatelleCompra(datelleCompraBack);
        assertThat(compra.getDatelleCompras()).containsOnly(datelleCompraBack);
        assertThat(datelleCompraBack.getCompra()).isEqualTo(compra);

        compra.removeDatelleCompra(datelleCompraBack);
        assertThat(compra.getDatelleCompras()).doesNotContain(datelleCompraBack);
        assertThat(datelleCompraBack.getCompra()).isNull();

        compra.datelleCompras(new HashSet<>(Set.of(datelleCompraBack)));
        assertThat(compra.getDatelleCompras()).containsOnly(datelleCompraBack);
        assertThat(datelleCompraBack.getCompra()).isEqualTo(compra);

        compra.setDatelleCompras(new HashSet<>());
        assertThat(compra.getDatelleCompras()).doesNotContain(datelleCompraBack);
        assertThat(datelleCompraBack.getCompra()).isNull();
    }

    @Test
    void usuarioTest() throws Exception {
        Compra compra = getCompraRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        compra.setUsuario(usuarioBack);
        assertThat(compra.getUsuario()).isEqualTo(usuarioBack);

        compra.usuario(null);
        assertThat(compra.getUsuario()).isNull();
    }
}
