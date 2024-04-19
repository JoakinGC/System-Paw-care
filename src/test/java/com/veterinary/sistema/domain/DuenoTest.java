package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.DuenoTestSamples.*;
import static com.veterinary.sistema.domain.MascotaTestSamples.*;
import static com.veterinary.sistema.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DuenoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dueno.class);
        Dueno dueno1 = getDuenoSample1();
        Dueno dueno2 = new Dueno();
        assertThat(dueno1).isNotEqualTo(dueno2);

        dueno2.setId(dueno1.getId());
        assertThat(dueno1).isEqualTo(dueno2);

        dueno2 = getDuenoSample2();
        assertThat(dueno1).isNotEqualTo(dueno2);
    }

    @Test
    void mascotaTest() throws Exception {
        Dueno dueno = getDuenoRandomSampleGenerator();
        Mascota mascotaBack = getMascotaRandomSampleGenerator();

        dueno.addMascota(mascotaBack);
        assertThat(dueno.getMascotas()).containsOnly(mascotaBack);
        assertThat(mascotaBack.getDueno()).isEqualTo(dueno);

        dueno.removeMascota(mascotaBack);
        assertThat(dueno.getMascotas()).doesNotContain(mascotaBack);
        assertThat(mascotaBack.getDueno()).isNull();

        dueno.mascotas(new HashSet<>(Set.of(mascotaBack)));
        assertThat(dueno.getMascotas()).containsOnly(mascotaBack);
        assertThat(mascotaBack.getDueno()).isEqualTo(dueno);

        dueno.setMascotas(new HashSet<>());
        assertThat(dueno.getMascotas()).doesNotContain(mascotaBack);
        assertThat(mascotaBack.getDueno()).isNull();
    }

    @Test
    void usuarioTest() throws Exception {
        Dueno dueno = getDuenoRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        dueno.setUsuario(usuarioBack);
        assertThat(dueno.getUsuario()).isEqualTo(usuarioBack);
        assertThat(usuarioBack.getDueno()).isEqualTo(dueno);

        dueno.usuario(null);
        assertThat(dueno.getUsuario()).isNull();
        assertThat(usuarioBack.getDueno()).isNull();
    }
}
