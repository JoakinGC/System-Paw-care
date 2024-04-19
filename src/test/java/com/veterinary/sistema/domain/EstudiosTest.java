package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.EstudiosTestSamples.*;
import static com.veterinary.sistema.domain.VeterinarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EstudiosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estudios.class);
        Estudios estudios1 = getEstudiosSample1();
        Estudios estudios2 = new Estudios();
        assertThat(estudios1).isNotEqualTo(estudios2);

        estudios2.setId(estudios1.getId());
        assertThat(estudios1).isEqualTo(estudios2);

        estudios2 = getEstudiosSample2();
        assertThat(estudios1).isNotEqualTo(estudios2);
    }

    @Test
    void veterinarioTest() throws Exception {
        Estudios estudios = getEstudiosRandomSampleGenerator();
        Veterinario veterinarioBack = getVeterinarioRandomSampleGenerator();

        estudios.addVeterinario(veterinarioBack);
        assertThat(estudios.getVeterinarios()).containsOnly(veterinarioBack);

        estudios.removeVeterinario(veterinarioBack);
        assertThat(estudios.getVeterinarios()).doesNotContain(veterinarioBack);

        estudios.veterinarios(new HashSet<>(Set.of(veterinarioBack)));
        assertThat(estudios.getVeterinarios()).containsOnly(veterinarioBack);

        estudios.setVeterinarios(new HashSet<>());
        assertThat(estudios.getVeterinarios()).doesNotContain(veterinarioBack);
    }
}
