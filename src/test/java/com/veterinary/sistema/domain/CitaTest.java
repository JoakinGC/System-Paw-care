package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.CitaTestSamples.*;
import static com.veterinary.sistema.domain.CuidadoraHotelTestSamples.*;
import static com.veterinary.sistema.domain.EsteticaTestSamples.*;
import static com.veterinary.sistema.domain.MascotaTestSamples.*;
import static com.veterinary.sistema.domain.VeterinarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CitaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cita.class);
        Cita cita1 = getCitaSample1();
        Cita cita2 = new Cita();
        assertThat(cita1).isNotEqualTo(cita2);

        cita2.setId(cita1.getId());
        assertThat(cita1).isEqualTo(cita2);

        cita2 = getCitaSample2();
        assertThat(cita1).isNotEqualTo(cita2);
    }

    @Test
    void esteticaTest() throws Exception {
        Cita cita = getCitaRandomSampleGenerator();
        Estetica esteticaBack = getEsteticaRandomSampleGenerator();

        cita.setEstetica(esteticaBack);
        assertThat(cita.getEstetica()).isEqualTo(esteticaBack);

        cita.estetica(null);
        assertThat(cita.getEstetica()).isNull();
    }

    @Test
    void cuidadoraHotelTest() throws Exception {
        Cita cita = getCitaRandomSampleGenerator();
        CuidadoraHotel cuidadoraHotelBack = getCuidadoraHotelRandomSampleGenerator();

        cita.setCuidadoraHotel(cuidadoraHotelBack);
        assertThat(cita.getCuidadoraHotel()).isEqualTo(cuidadoraHotelBack);

        cita.cuidadoraHotel(null);
        assertThat(cita.getCuidadoraHotel()).isNull();
    }

    @Test
    void veterinarioTest() throws Exception {
        Cita cita = getCitaRandomSampleGenerator();
        Veterinario veterinarioBack = getVeterinarioRandomSampleGenerator();

        cita.setVeterinario(veterinarioBack);
        assertThat(cita.getVeterinario()).isEqualTo(veterinarioBack);

        cita.veterinario(null);
        assertThat(cita.getVeterinario()).isNull();
    }

    @Test
    void mascotaTest() throws Exception {
        Cita cita = getCitaRandomSampleGenerator();
        Mascota mascotaBack = getMascotaRandomSampleGenerator();

        cita.addMascota(mascotaBack);
        assertThat(cita.getMascotas()).containsOnly(mascotaBack);
        assertThat(mascotaBack.getCitas()).containsOnly(cita);

        cita.removeMascota(mascotaBack);
        assertThat(cita.getMascotas()).doesNotContain(mascotaBack);
        assertThat(mascotaBack.getCitas()).doesNotContain(cita);

        cita.mascotas(new HashSet<>(Set.of(mascotaBack)));
        assertThat(cita.getMascotas()).containsOnly(mascotaBack);
        assertThat(mascotaBack.getCitas()).containsOnly(cita);

        cita.setMascotas(new HashSet<>());
        assertThat(cita.getMascotas()).doesNotContain(mascotaBack);
        assertThat(mascotaBack.getCitas()).doesNotContain(cita);
    }
}
