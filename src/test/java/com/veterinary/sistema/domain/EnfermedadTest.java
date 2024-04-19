package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.EnfermedadTestSamples.*;
import static com.veterinary.sistema.domain.EspecieTestSamples.*;
import static com.veterinary.sistema.domain.FactoresTestSamples.*;
import static com.veterinary.sistema.domain.HistorialTestSamples.*;
import static com.veterinary.sistema.domain.RazaTestSamples.*;
import static com.veterinary.sistema.domain.TerapiaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EnfermedadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enfermedad.class);
        Enfermedad enfermedad1 = getEnfermedadSample1();
        Enfermedad enfermedad2 = new Enfermedad();
        assertThat(enfermedad1).isNotEqualTo(enfermedad2);

        enfermedad2.setId(enfermedad1.getId());
        assertThat(enfermedad1).isEqualTo(enfermedad2);

        enfermedad2 = getEnfermedadSample2();
        assertThat(enfermedad1).isNotEqualTo(enfermedad2);
    }

    @Test
    void razaTest() throws Exception {
        Enfermedad enfermedad = getEnfermedadRandomSampleGenerator();
        Raza razaBack = getRazaRandomSampleGenerator();

        enfermedad.addRaza(razaBack);
        assertThat(enfermedad.getRazas()).containsOnly(razaBack);

        enfermedad.removeRaza(razaBack);
        assertThat(enfermedad.getRazas()).doesNotContain(razaBack);

        enfermedad.razas(new HashSet<>(Set.of(razaBack)));
        assertThat(enfermedad.getRazas()).containsOnly(razaBack);

        enfermedad.setRazas(new HashSet<>());
        assertThat(enfermedad.getRazas()).doesNotContain(razaBack);
    }

    @Test
    void especieTest() throws Exception {
        Enfermedad enfermedad = getEnfermedadRandomSampleGenerator();
        Especie especieBack = getEspecieRandomSampleGenerator();

        enfermedad.addEspecie(especieBack);
        assertThat(enfermedad.getEspecies()).containsOnly(especieBack);

        enfermedad.removeEspecie(especieBack);
        assertThat(enfermedad.getEspecies()).doesNotContain(especieBack);

        enfermedad.especies(new HashSet<>(Set.of(especieBack)));
        assertThat(enfermedad.getEspecies()).containsOnly(especieBack);

        enfermedad.setEspecies(new HashSet<>());
        assertThat(enfermedad.getEspecies()).doesNotContain(especieBack);
    }

    @Test
    void terapiaTest() throws Exception {
        Enfermedad enfermedad = getEnfermedadRandomSampleGenerator();
        Terapia terapiaBack = getTerapiaRandomSampleGenerator();

        enfermedad.addTerapia(terapiaBack);
        assertThat(enfermedad.getTerapias()).containsOnly(terapiaBack);

        enfermedad.removeTerapia(terapiaBack);
        assertThat(enfermedad.getTerapias()).doesNotContain(terapiaBack);

        enfermedad.terapias(new HashSet<>(Set.of(terapiaBack)));
        assertThat(enfermedad.getTerapias()).containsOnly(terapiaBack);

        enfermedad.setTerapias(new HashSet<>());
        assertThat(enfermedad.getTerapias()).doesNotContain(terapiaBack);
    }

    @Test
    void factoresTest() throws Exception {
        Enfermedad enfermedad = getEnfermedadRandomSampleGenerator();
        Factores factoresBack = getFactoresRandomSampleGenerator();

        enfermedad.addFactores(factoresBack);
        assertThat(enfermedad.getFactores()).containsOnly(factoresBack);

        enfermedad.removeFactores(factoresBack);
        assertThat(enfermedad.getFactores()).doesNotContain(factoresBack);

        enfermedad.factores(new HashSet<>(Set.of(factoresBack)));
        assertThat(enfermedad.getFactores()).containsOnly(factoresBack);

        enfermedad.setFactores(new HashSet<>());
        assertThat(enfermedad.getFactores()).doesNotContain(factoresBack);
    }

    @Test
    void historialTest() throws Exception {
        Enfermedad enfermedad = getEnfermedadRandomSampleGenerator();
        Historial historialBack = getHistorialRandomSampleGenerator();

        enfermedad.addHistorial(historialBack);
        assertThat(enfermedad.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getEnfermedads()).containsOnly(enfermedad);

        enfermedad.removeHistorial(historialBack);
        assertThat(enfermedad.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getEnfermedads()).doesNotContain(enfermedad);

        enfermedad.historials(new HashSet<>(Set.of(historialBack)));
        assertThat(enfermedad.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getEnfermedads()).containsOnly(enfermedad);

        enfermedad.setHistorials(new HashSet<>());
        assertThat(enfermedad.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getEnfermedads()).doesNotContain(enfermedad);
    }
}
