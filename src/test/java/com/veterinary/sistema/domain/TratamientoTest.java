package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.HistorialTestSamples.*;
import static com.veterinary.sistema.domain.TratamientoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TratamientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tratamiento.class);
        Tratamiento tratamiento1 = getTratamientoSample1();
        Tratamiento tratamiento2 = new Tratamiento();
        assertThat(tratamiento1).isNotEqualTo(tratamiento2);

        tratamiento2.setId(tratamiento1.getId());
        assertThat(tratamiento1).isEqualTo(tratamiento2);

        tratamiento2 = getTratamientoSample2();
        assertThat(tratamiento1).isNotEqualTo(tratamiento2);
    }

    @Test
    void historialTest() throws Exception {
        Tratamiento tratamiento = getTratamientoRandomSampleGenerator();
        Historial historialBack = getHistorialRandomSampleGenerator();

        tratamiento.setHistorial(historialBack);
        assertThat(tratamiento.getHistorial()).isEqualTo(historialBack);

        tratamiento.historial(null);
        assertThat(tratamiento.getHistorial()).isNull();
    }
}
