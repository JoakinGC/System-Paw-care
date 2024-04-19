package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EspecieTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Especie getEspecieSample1() {
        return new Especie().id(1L).nombre("nombre1").nombreCientifico("nombreCientifico1");
    }

    public static Especie getEspecieSample2() {
        return new Especie().id(2L).nombre("nombre2").nombreCientifico("nombreCientifico2");
    }

    public static Especie getEspecieRandomSampleGenerator() {
        return new Especie()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .nombreCientifico(UUID.randomUUID().toString());
    }
}
