package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TerapiaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Terapia getTerapiaSample1() {
        return new Terapia().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static Terapia getTerapiaSample2() {
        return new Terapia().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static Terapia getTerapiaRandomSampleGenerator() {
        return new Terapia().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString()).descripcion(UUID.randomUUID().toString());
    }
}
