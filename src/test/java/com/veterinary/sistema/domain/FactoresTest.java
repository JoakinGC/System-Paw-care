package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.EnfermedadTestSamples.*;
import static com.veterinary.sistema.domain.FactoresTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FactoresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factores.class);
        Factores factores1 = getFactoresSample1();
        Factores factores2 = new Factores();
        assertThat(factores1).isNotEqualTo(factores2);

        factores2.setId(factores1.getId());
        assertThat(factores1).isEqualTo(factores2);

        factores2 = getFactoresSample2();
        assertThat(factores1).isNotEqualTo(factores2);
    }

    @Test
    void enfermedadTest() throws Exception {
        Factores factores = getFactoresRandomSampleGenerator();
        Enfermedad enfermedadBack = getEnfermedadRandomSampleGenerator();

        factores.addEnfermedad(enfermedadBack);
        assertThat(factores.getEnfermedads()).containsOnly(enfermedadBack);
        assertThat(enfermedadBack.getFactores()).containsOnly(factores);

        factores.removeEnfermedad(enfermedadBack);
        assertThat(factores.getEnfermedads()).doesNotContain(enfermedadBack);
        assertThat(enfermedadBack.getFactores()).doesNotContain(factores);

        factores.enfermedads(new HashSet<>(Set.of(enfermedadBack)));
        assertThat(factores.getEnfermedads()).containsOnly(enfermedadBack);
        assertThat(enfermedadBack.getFactores()).containsOnly(factores);

        factores.setEnfermedads(new HashSet<>());
        assertThat(factores.getEnfermedads()).doesNotContain(enfermedadBack);
        assertThat(enfermedadBack.getFactores()).doesNotContain(factores);
    }
}
