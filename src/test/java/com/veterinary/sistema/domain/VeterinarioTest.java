package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.CitaTestSamples.*;
import static com.veterinary.sistema.domain.EstudiosTestSamples.*;
import static com.veterinary.sistema.domain.HistorialTestSamples.*;
import static com.veterinary.sistema.domain.UsuarioTestSamples.*;
import static com.veterinary.sistema.domain.VeterinarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VeterinarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Veterinario.class);
        Veterinario veterinario1 = getVeterinarioSample1();
        Veterinario veterinario2 = new Veterinario();
        assertThat(veterinario1).isNotEqualTo(veterinario2);

        veterinario2.setId(veterinario1.getId());
        assertThat(veterinario1).isEqualTo(veterinario2);

        veterinario2 = getVeterinarioSample2();
        assertThat(veterinario1).isNotEqualTo(veterinario2);
    }

    @Test
    void historialTest() throws Exception {
        Veterinario veterinario = getVeterinarioRandomSampleGenerator();
        Historial historialBack = getHistorialRandomSampleGenerator();

        veterinario.addHistorial(historialBack);
        assertThat(veterinario.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getVeterinario()).isEqualTo(veterinario);

        veterinario.removeHistorial(historialBack);
        assertThat(veterinario.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getVeterinario()).isNull();

        veterinario.historials(new HashSet<>(Set.of(historialBack)));
        assertThat(veterinario.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getVeterinario()).isEqualTo(veterinario);

        veterinario.setHistorials(new HashSet<>());
        assertThat(veterinario.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getVeterinario()).isNull();
    }

    @Test
    void citaTest() throws Exception {
        Veterinario veterinario = getVeterinarioRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        veterinario.addCita(citaBack);
        assertThat(veterinario.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getVeterinario()).isEqualTo(veterinario);

        veterinario.removeCita(citaBack);
        assertThat(veterinario.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getVeterinario()).isNull();

        veterinario.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(veterinario.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getVeterinario()).isEqualTo(veterinario);

        veterinario.setCitas(new HashSet<>());
        assertThat(veterinario.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getVeterinario()).isNull();
    }

    @Test
    void usuarioTest() throws Exception {
        Veterinario veterinario = getVeterinarioRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        veterinario.setUsuario(usuarioBack);
        assertThat(veterinario.getUsuario()).isEqualTo(usuarioBack);
        assertThat(usuarioBack.getVeterinario()).isEqualTo(veterinario);

        veterinario.usuario(null);
        assertThat(veterinario.getUsuario()).isNull();
        assertThat(usuarioBack.getVeterinario()).isNull();
    }

    @Test
    void estudiosTest() throws Exception {
        Veterinario veterinario = getVeterinarioRandomSampleGenerator();
        Estudios estudiosBack = getEstudiosRandomSampleGenerator();

        veterinario.addEstudios(estudiosBack);
        assertThat(veterinario.getEstudios()).containsOnly(estudiosBack);
        assertThat(estudiosBack.getVeterinarios()).containsOnly(veterinario);

        veterinario.removeEstudios(estudiosBack);
        assertThat(veterinario.getEstudios()).doesNotContain(estudiosBack);
        assertThat(estudiosBack.getVeterinarios()).doesNotContain(veterinario);

        veterinario.estudios(new HashSet<>(Set.of(estudiosBack)));
        assertThat(veterinario.getEstudios()).containsOnly(estudiosBack);
        assertThat(estudiosBack.getVeterinarios()).containsOnly(veterinario);

        veterinario.setEstudios(new HashSet<>());
        assertThat(veterinario.getEstudios()).doesNotContain(estudiosBack);
        assertThat(estudiosBack.getVeterinarios()).doesNotContain(veterinario);
    }
}
