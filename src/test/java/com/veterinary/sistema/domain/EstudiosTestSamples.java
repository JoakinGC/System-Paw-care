package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EstudiosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Estudios getEstudiosSample1() {
        return new Estudios().id(1L).nombre("nombre1").nombreInsituto("nombreInsituto1");
    }

    public static Estudios getEstudiosSample2() {
        return new Estudios().id(2L).nombre("nombre2").nombreInsituto("nombreInsituto2");
    }

    public static Estudios getEstudiosRandomSampleGenerator() {
        return new Estudios()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .nombreInsituto(UUID.randomUUID().toString());
    }
}
