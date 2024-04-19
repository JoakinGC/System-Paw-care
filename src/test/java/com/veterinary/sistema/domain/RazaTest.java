package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.EnfermedadTestSamples.*;
import static com.veterinary.sistema.domain.MascotaTestSamples.*;
import static com.veterinary.sistema.domain.RazaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RazaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Raza.class);
        Raza raza1 = getRazaSample1();
        Raza raza2 = new Raza();
        assertThat(raza1).isNotEqualTo(raza2);

        raza2.setId(raza1.getId());
        assertThat(raza1).isEqualTo(raza2);

        raza2 = getRazaSample2();
        assertThat(raza1).isNotEqualTo(raza2);
    }

    @Test
    void mascotaTest() throws Exception {
        Raza raza = getRazaRandomSampleGenerator();
        Mascota mascotaBack = getMascotaRandomSampleGenerator();

        raza.addMascota(mascotaBack);
        assertThat(raza.getMascotas()).containsOnly(mascotaBack);
        assertThat(mascotaBack.getRaza()).isEqualTo(raza);

        raza.removeMascota(mascotaBack);
        assertThat(raza.getMascotas()).doesNotContain(mascotaBack);
        assertThat(mascotaBack.getRaza()).isNull();

        raza.mascotas(new HashSet<>(Set.of(mascotaBack)));
        assertThat(raza.getMascotas()).containsOnly(mascotaBack);
        assertThat(mascotaBack.getRaza()).isEqualTo(raza);

        raza.setMascotas(new HashSet<>());
        assertThat(raza.getMascotas()).doesNotContain(mascotaBack);
        assertThat(mascotaBack.getRaza()).isNull();
    }

    @Test
    void enfermedadTest() throws Exception {
        Raza raza = getRazaRandomSampleGenerator();
        Enfermedad enfermedadBack = getEnfermedadRandomSampleGenerator();

        raza.addEnfermedad(enfermedadBack);
        assertThat(raza.getEnfermedads()).containsOnly(enfermedadBack);
        assertThat(enfermedadBack.getRazas()).containsOnly(raza);

        raza.removeEnfermedad(enfermedadBack);
        assertThat(raza.getEnfermedads()).doesNotContain(enfermedadBack);
        assertThat(enfermedadBack.getRazas()).doesNotContain(raza);

        raza.enfermedads(new HashSet<>(Set.of(enfermedadBack)));
        assertThat(raza.getEnfermedads()).containsOnly(enfermedadBack);
        assertThat(enfermedadBack.getRazas()).containsOnly(raza);

        raza.setEnfermedads(new HashSet<>());
        assertThat(raza.getEnfermedads()).doesNotContain(enfermedadBack);
        assertThat(enfermedadBack.getRazas()).doesNotContain(raza);
    }
}
