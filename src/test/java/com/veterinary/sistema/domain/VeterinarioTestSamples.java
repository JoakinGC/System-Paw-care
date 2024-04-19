package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VeterinarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Veterinario getVeterinarioSample1() {
        return new Veterinario()
            .id(1L)
            .nombre("nombre1")
            .apellido("apellido1")
            .direccion("direccion1")
            .telefono("telefono1")
            .especilidad("especilidad1");
    }

    public static Veterinario getVeterinarioSample2() {
        return new Veterinario()
            .id(2L)
            .nombre("nombre2")
            .apellido("apellido2")
            .direccion("direccion2")
            .telefono("telefono2")
            .especilidad("especilidad2");
    }

    public static Veterinario getVeterinarioRandomSampleGenerator() {
        return new Veterinario()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .especilidad(UUID.randomUUID().toString());
    }
}
