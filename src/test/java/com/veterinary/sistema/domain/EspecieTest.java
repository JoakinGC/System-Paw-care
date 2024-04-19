package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.EnfermedadTestSamples.*;
import static com.veterinary.sistema.domain.EspecieTestSamples.*;
import static com.veterinary.sistema.domain.MascotaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EspecieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especie.class);
        Especie especie1 = getEspecieSample1();
        Especie especie2 = new Especie();
        assertThat(especie1).isNotEqualTo(especie2);

        especie2.setId(especie1.getId());
        assertThat(especie1).isEqualTo(especie2);

        especie2 = getEspecieSample2();
        assertThat(especie1).isNotEqualTo(especie2);
    }

    @Test
    void mascotaTest() throws Exception {
        Especie especie = getEspecieRandomSampleGenerator();
        Mascota mascotaBack = getMascotaRandomSampleGenerator();

        especie.addMascota(mascotaBack);
        assertThat(especie.getMascotas()).containsOnly(mascotaBack);
        assertThat(mascotaBack.getEspecie()).isEqualTo(especie);

        especie.removeMascota(mascotaBack);
        assertThat(especie.getMascotas()).doesNotContain(mascotaBack);
        assertThat(mascotaBack.getEspecie()).isNull();

        especie.mascotas(new HashSet<>(Set.of(mascotaBack)));
        assertThat(especie.getMascotas()).containsOnly(mascotaBack);
        assertThat(mascotaBack.getEspecie()).isEqualTo(especie);

        especie.setMascotas(new HashSet<>());
        assertThat(especie.getMascotas()).doesNotContain(mascotaBack);
        assertThat(mascotaBack.getEspecie()).isNull();
    }

    @Test
    void enfermedadTest() throws Exception {
        Especie especie = getEspecieRandomSampleGenerator();
        Enfermedad enfermedadBack = getEnfermedadRandomSampleGenerator();

        especie.addEnfermedad(enfermedadBack);
        assertThat(especie.getEnfermedads()).containsOnly(enfermedadBack);
        assertThat(enfermedadBack.getEspecies()).containsOnly(especie);

        especie.removeEnfermedad(enfermedadBack);
        assertThat(especie.getEnfermedads()).doesNotContain(enfermedadBack);
        assertThat(enfermedadBack.getEspecies()).doesNotContain(especie);

        especie.enfermedads(new HashSet<>(Set.of(enfermedadBack)));
        assertThat(especie.getEnfermedads()).containsOnly(enfermedadBack);
        assertThat(enfermedadBack.getEspecies()).containsOnly(especie);

        especie.setEnfermedads(new HashSet<>());
        assertThat(especie.getEnfermedads()).doesNotContain(enfermedadBack);
        assertThat(enfermedadBack.getEspecies()).doesNotContain(especie);
    }
}
