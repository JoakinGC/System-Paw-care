package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.CitaTestSamples.*;
import static com.veterinary.sistema.domain.CuidadoraHotelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CuidadoraHotelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuidadoraHotel.class);
        CuidadoraHotel cuidadoraHotel1 = getCuidadoraHotelSample1();
        CuidadoraHotel cuidadoraHotel2 = new CuidadoraHotel();
        assertThat(cuidadoraHotel1).isNotEqualTo(cuidadoraHotel2);

        cuidadoraHotel2.setId(cuidadoraHotel1.getId());
        assertThat(cuidadoraHotel1).isEqualTo(cuidadoraHotel2);

        cuidadoraHotel2 = getCuidadoraHotelSample2();
        assertThat(cuidadoraHotel1).isNotEqualTo(cuidadoraHotel2);
    }

    @Test
    void citaTest() throws Exception {
        CuidadoraHotel cuidadoraHotel = getCuidadoraHotelRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        cuidadoraHotel.addCita(citaBack);
        assertThat(cuidadoraHotel.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getCuidadoraHotel()).isEqualTo(cuidadoraHotel);

        cuidadoraHotel.removeCita(citaBack);
        assertThat(cuidadoraHotel.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getCuidadoraHotel()).isNull();

        cuidadoraHotel.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(cuidadoraHotel.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getCuidadoraHotel()).isEqualTo(cuidadoraHotel);

        cuidadoraHotel.setCitas(new HashSet<>());
        assertThat(cuidadoraHotel.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getCuidadoraHotel()).isNull();
    }
}
