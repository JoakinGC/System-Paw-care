package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.EnfermedadTestSamples.*;
import static com.veterinary.sistema.domain.HistorialTestSamples.*;
import static com.veterinary.sistema.domain.MascotaTestSamples.*;
import static com.veterinary.sistema.domain.MedicamentoTestSamples.*;
import static com.veterinary.sistema.domain.TratamientoTestSamples.*;
import static com.veterinary.sistema.domain.VeterinarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HistorialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Historial.class);
        Historial historial1 = getHistorialSample1();
        Historial historial2 = new Historial();
        assertThat(historial1).isNotEqualTo(historial2);

        historial2.setId(historial1.getId());
        assertThat(historial1).isEqualTo(historial2);

        historial2 = getHistorialSample2();
        assertThat(historial1).isNotEqualTo(historial2);
    }

    @Test
    void tratamientoTest() throws Exception {
        Historial historial = getHistorialRandomSampleGenerator();
        Tratamiento tratamientoBack = getTratamientoRandomSampleGenerator();

        historial.addTratamiento(tratamientoBack);
        assertThat(historial.getTratamientos()).containsOnly(tratamientoBack);
        assertThat(tratamientoBack.getHistorial()).isEqualTo(historial);

        historial.removeTratamiento(tratamientoBack);
        assertThat(historial.getTratamientos()).doesNotContain(tratamientoBack);
        assertThat(tratamientoBack.getHistorial()).isNull();

        historial.tratamientos(new HashSet<>(Set.of(tratamientoBack)));
        assertThat(historial.getTratamientos()).containsOnly(tratamientoBack);
        assertThat(tratamientoBack.getHistorial()).isEqualTo(historial);

        historial.setTratamientos(new HashSet<>());
        assertThat(historial.getTratamientos()).doesNotContain(tratamientoBack);
        assertThat(tratamientoBack.getHistorial()).isNull();
    }

    @Test
    void medicamentoTest() throws Exception {
        Historial historial = getHistorialRandomSampleGenerator();
        Medicamento medicamentoBack = getMedicamentoRandomSampleGenerator();

        historial.addMedicamento(medicamentoBack);
        assertThat(historial.getMedicamentos()).containsOnly(medicamentoBack);

        historial.removeMedicamento(medicamentoBack);
        assertThat(historial.getMedicamentos()).doesNotContain(medicamentoBack);

        historial.medicamentos(new HashSet<>(Set.of(medicamentoBack)));
        assertThat(historial.getMedicamentos()).containsOnly(medicamentoBack);

        historial.setMedicamentos(new HashSet<>());
        assertThat(historial.getMedicamentos()).doesNotContain(medicamentoBack);
    }

    @Test
    void enfermedadTest() throws Exception {
        Historial historial = getHistorialRandomSampleGenerator();
        Enfermedad enfermedadBack = getEnfermedadRandomSampleGenerator();

        historial.addEnfermedad(enfermedadBack);
        assertThat(historial.getEnfermedads()).containsOnly(enfermedadBack);

        historial.removeEnfermedad(enfermedadBack);
        assertThat(historial.getEnfermedads()).doesNotContain(enfermedadBack);

        historial.enfermedads(new HashSet<>(Set.of(enfermedadBack)));
        assertThat(historial.getEnfermedads()).containsOnly(enfermedadBack);

        historial.setEnfermedads(new HashSet<>());
        assertThat(historial.getEnfermedads()).doesNotContain(enfermedadBack);
    }

    @Test
    void veterinarioTest() throws Exception {
        Historial historial = getHistorialRandomSampleGenerator();
        Veterinario veterinarioBack = getVeterinarioRandomSampleGenerator();

        historial.setVeterinario(veterinarioBack);
        assertThat(historial.getVeterinario()).isEqualTo(veterinarioBack);

        historial.veterinario(null);
        assertThat(historial.getVeterinario()).isNull();
    }

    @Test
    void mascotaTest() throws Exception {
        Historial historial = getHistorialRandomSampleGenerator();
        Mascota mascotaBack = getMascotaRandomSampleGenerator();

        historial.setMascota(mascotaBack);
        assertThat(historial.getMascota()).isEqualTo(mascotaBack);

        historial.mascota(null);
        assertThat(historial.getMascota()).isNull();
    }
}
