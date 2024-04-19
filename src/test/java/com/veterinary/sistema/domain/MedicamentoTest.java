package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.HistorialTestSamples.*;
import static com.veterinary.sistema.domain.MedicamentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicamento.class);
        Medicamento medicamento1 = getMedicamentoSample1();
        Medicamento medicamento2 = new Medicamento();
        assertThat(medicamento1).isNotEqualTo(medicamento2);

        medicamento2.setId(medicamento1.getId());
        assertThat(medicamento1).isEqualTo(medicamento2);

        medicamento2 = getMedicamentoSample2();
        assertThat(medicamento1).isNotEqualTo(medicamento2);
    }

    @Test
    void historialTest() throws Exception {
        Medicamento medicamento = getMedicamentoRandomSampleGenerator();
        Historial historialBack = getHistorialRandomSampleGenerator();

        medicamento.addHistorial(historialBack);
        assertThat(medicamento.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getMedicamentos()).containsOnly(medicamento);

        medicamento.removeHistorial(historialBack);
        assertThat(medicamento.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getMedicamentos()).doesNotContain(medicamento);

        medicamento.historials(new HashSet<>(Set.of(historialBack)));
        assertThat(medicamento.getHistorials()).containsOnly(historialBack);
        assertThat(historialBack.getMedicamentos()).containsOnly(medicamento);

        medicamento.setHistorials(new HashSet<>());
        assertThat(medicamento.getHistorials()).doesNotContain(historialBack);
        assertThat(historialBack.getMedicamentos()).doesNotContain(medicamento);
    }
}
