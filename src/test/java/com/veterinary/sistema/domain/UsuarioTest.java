package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.CompraTestSamples.*;
import static com.veterinary.sistema.domain.DuenoTestSamples.*;
import static com.veterinary.sistema.domain.EsteticaTestSamples.*;
import static com.veterinary.sistema.domain.UsuarioTestSamples.*;
import static com.veterinary.sistema.domain.VeterinarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usuario.class);
        Usuario usuario1 = getUsuarioSample1();
        Usuario usuario2 = new Usuario();
        assertThat(usuario1).isNotEqualTo(usuario2);

        usuario2.setId(usuario1.getId());
        assertThat(usuario1).isEqualTo(usuario2);

        usuario2 = getUsuarioSample2();
        assertThat(usuario1).isNotEqualTo(usuario2);
    }

    @Test
    void esteticaTest() throws Exception {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Estetica esteticaBack = getEsteticaRandomSampleGenerator();

        usuario.setEstetica(esteticaBack);
        assertThat(usuario.getEstetica()).isEqualTo(esteticaBack);

        usuario.estetica(null);
        assertThat(usuario.getEstetica()).isNull();
    }

    @Test
    void veterinarioTest() throws Exception {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Veterinario veterinarioBack = getVeterinarioRandomSampleGenerator();

        usuario.setVeterinario(veterinarioBack);
        assertThat(usuario.getVeterinario()).isEqualTo(veterinarioBack);

        usuario.veterinario(null);
        assertThat(usuario.getVeterinario()).isNull();
    }

    @Test
    void duenoTest() throws Exception {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Dueno duenoBack = getDuenoRandomSampleGenerator();

        usuario.setDueno(duenoBack);
        assertThat(usuario.getDueno()).isEqualTo(duenoBack);

        usuario.dueno(null);
        assertThat(usuario.getDueno()).isNull();
    }

    @Test
    void compraTest() throws Exception {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Compra compraBack = getCompraRandomSampleGenerator();

        usuario.addCompra(compraBack);
        assertThat(usuario.getCompras()).containsOnly(compraBack);
        assertThat(compraBack.getUsuario()).isEqualTo(usuario);

        usuario.removeCompra(compraBack);
        assertThat(usuario.getCompras()).doesNotContain(compraBack);
        assertThat(compraBack.getUsuario()).isNull();

        usuario.compras(new HashSet<>(Set.of(compraBack)));
        assertThat(usuario.getCompras()).containsOnly(compraBack);
        assertThat(compraBack.getUsuario()).isEqualTo(usuario);

        usuario.setCompras(new HashSet<>());
        assertThat(usuario.getCompras()).doesNotContain(compraBack);
        assertThat(compraBack.getUsuario()).isNull();
    }
}
