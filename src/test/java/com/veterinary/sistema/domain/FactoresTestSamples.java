package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FactoresTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Factores getFactoresSample1() {
        return new Factores().id(1L).nombre("nombre1").tipo("tipo1").descripcion("descripcion1");
    }

    public static Factores getFactoresSample2() {
        return new Factores().id(2L).nombre("nombre2").tipo("tipo2").descripcion("descripcion2");
    }

    public static Factores getFactoresRandomSampleGenerator() {
        return new Factores()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .tipo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
