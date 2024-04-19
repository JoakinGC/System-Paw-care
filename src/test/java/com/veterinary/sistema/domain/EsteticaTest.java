package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.CitaTestSamples.*;
import static com.veterinary.sistema.domain.EsteticaTestSamples.*;
import static com.veterinary.sistema.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EsteticaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estetica.class);
        Estetica estetica1 = getEsteticaSample1();
        Estetica estetica2 = new Estetica();
        assertThat(estetica1).isNotEqualTo(estetica2);

        estetica2.setId(estetica1.getId());
        assertThat(estetica1).isEqualTo(estetica2);

        estetica2 = getEsteticaSample2();
        assertThat(estetica1).isNotEqualTo(estetica2);
    }

    @Test
    void citaTest() throws Exception {
        Estetica estetica = getEsteticaRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        estetica.addCita(citaBack);
        assertThat(estetica.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getEstetica()).isEqualTo(estetica);

        estetica.removeCita(citaBack);
        assertThat(estetica.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getEstetica()).isNull();

        estetica.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(estetica.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getEstetica()).isEqualTo(estetica);

        estetica.setCitas(new HashSet<>());
        assertThat(estetica.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getEstetica()).isNull();
    }

    @Test
    void usuarioTest() throws Exception {
        Estetica estetica = getEsteticaRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        estetica.setUsuario(usuarioBack);
        assertThat(estetica.getUsuario()).isEqualTo(usuarioBack);
        assertThat(usuarioBack.getEstetica()).isEqualTo(estetica);

        estetica.usuario(null);
        assertThat(estetica.getUsuario()).isNull();
        assertThat(usuarioBack.getEstetica()).isNull();
    }
}
