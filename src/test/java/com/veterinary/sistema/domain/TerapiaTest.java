package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.EnfermedadTestSamples.*;
import static com.veterinary.sistema.domain.TerapiaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TerapiaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Terapia.class);
        Terapia terapia1 = getTerapiaSample1();
        Terapia terapia2 = new Terapia();
        assertThat(terapia1).isNotEqualTo(terapia2);

        terapia2.setId(terapia1.getId());
        assertThat(terapia1).isEqualTo(terapia2);

        terapia2 = getTerapiaSample2();
        assertThat(terapia1).isNotEqualTo(terapia2);
    }

    @Test
    void enfermedadTest() throws Exception {
        Terapia terapia = getTerapiaRandomSampleGenerator();
        Enfermedad enfermedadBack = getEnfermedadRandomSampleGenerator();

        terapia.addEnfermedad(enfermedadBack);
        assertThat(terapia.getEnfermedads()).containsOnly(enfermedadBack);
        assertThat(enfermedadBack.getTerapias()).containsOnly(terapia);

        terapia.removeEnfermedad(enfermedadBack);
        assertThat(terapia.getEnfermedads()).doesNotContain(enfermedadBack);
        assertThat(enfermedadBack.getTerapias()).doesNotContain(terapia);

        terapia.enfermedads(new HashSet<>(Set.of(enfermedadBack)));
        assertThat(terapia.getEnfermedads()).containsOnly(enfermedadBack);
        assertThat(enfermedadBack.getTerapias()).containsOnly(terapia);

        terapia.setEnfermedads(new HashSet<>());
        assertThat(terapia.getEnfermedads()).doesNotContain(enfermedadBack);
        assertThat(enfermedadBack.getTerapias()).doesNotContain(terapia);
    }
}
