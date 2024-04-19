package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RazaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Raza getRazaSample1() {
        return new Raza().id(1L).nombre("nombre1").nombreCientifico("nombreCientifico1");
    }

    public static Raza getRazaSample2() {
        return new Raza().id(2L).nombre("nombre2").nombreCientifico("nombreCientifico2");
    }

    public static Raza getRazaRandomSampleGenerator() {
        return new Raza()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .nombreCientifico(UUID.randomUUID().toString());
    }
}
