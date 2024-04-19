package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EsteticaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Estetica getEsteticaSample1() {
        return new Estetica().id(1L).nombre("nombre1").direcion("direcion1").telefono("telefono1");
    }

    public static Estetica getEsteticaSample2() {
        return new Estetica().id(2L).nombre("nombre2").direcion("direcion2").telefono("telefono2");
    }

    public static Estetica getEsteticaRandomSampleGenerator() {
        return new Estetica()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .direcion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
