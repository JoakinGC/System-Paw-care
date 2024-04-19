package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.CitaTestSamples.*;
import static com.veterinary.sistema.domain.DuenoTestSamples.*;
import static com.veterinary.sistema.domain.EspecieTestSamples.*;
import static com.veterinary.sistema.domain.HistorialTestSamples.*;
import static com.veterinary.sistema.domain.MascotaTestSamples.*;
import static com.veterinary.sistema.domain.RazaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MascotaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mascota.class);
        Mascota mascota1 = getMascotaSample1();
        Mascota mascota2 = new Mascota();
        assertThat(mascota1).isNotEqualTo(mascota2);

        mascota2.setId(mascota1.getId());
        assertThat(mascota1).isEqualTo(mascota2);

        mascota2 = getMascotaSample2();
        assertThat(mascota1).isNotEqualTo(mascota2);
    }

    @Test
    void historialTest() throws Exception {
        Mascota mascota = getMascotaRandomSampleGenerator();
        Historial historialBack = getHistorialRandomSampleGenerator();

        mascota.addHistorial(historialBack);
        assertThat(mascota.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getMascota()).isEqualTo(mascota);

        mascota.removeHistorial(historialBack);
        assertThat(mascota.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getMascota()).isNull();

        mascota.historials(new HashSet<>(Set.of(historialBack)));
        assertThat(mascota.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getMascota()).isEqualTo(mascota);

        mascota.setHistorials(new HashSet<>());
        assertThat(mascota.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getMascota()).isNull();
    }

    @Test
    void citaTest() throws Exception {
        Mascota mascota = getMascotaRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        mascota.addCita(citaBack);
        assertThat(mascota.getCitas()).containsOnly(citaBack);

        mascota.removeCita(citaBack);
        assertThat(mascota.getCitas()).doesNotContain(citaBack);

        mascota.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(mascota.getCitas()).containsOnly(citaBack);

        mascota.setCitas(new HashSet<>());
        assertThat(mascota.getCitas()).doesNotContain(citaBack);
    }

    @Test
    void duenoTest() throws Exception {
        Mascota mascota = getMascotaRandomSampleGenerator();
        Dueno duenoBack = getDuenoRandomSampleGenerator();

        mascota.setDueno(duenoBack);
        assertThat(mascota.getDueno()).isEqualTo(duenoBack);

        mascota.dueno(null);
        assertThat(mascota.getDueno()).isNull();
    }

    @Test
    void especieTest() throws Exception {
        Mascota mascota = getMascotaRandomSampleGenerator();
        Especie especieBack = getEspecieRandomSampleGenerator();

        mascota.setEspecie(especieBack);
        assertThat(mascota.getEspecie()).isEqualTo(especieBack);

        mascota.especie(null);
        assertThat(mascota.getEspecie()).isNull();
    }

    @Test
    void razaTest() throws Exception {
        Mascota mascota = getMascotaRandomSampleGenerator();
        Raza razaBack = getRazaRandomSampleGenerator();

        mascota.setRaza(razaBack);
        assertThat(mascota.getRaza()).isEqualTo(razaBack);

        mascota.raza(null);
        assertThat(mascota.getRaza()).isNull();
    }
}
