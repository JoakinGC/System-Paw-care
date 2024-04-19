package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DuenoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Dueno getDuenoSample1() {
        return new Dueno().id(1L).nombre("nombre1").apellido("apellido1").direccion("direccion1").telefono("telefono1");
    }

    public static Dueno getDuenoSample2() {
        return new Dueno().id(2L).nombre("nombre2").apellido("apellido2").direccion("direccion2").telefono("telefono2");
    }

    public static Dueno getDuenoRandomSampleGenerator() {
        return new Dueno()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
