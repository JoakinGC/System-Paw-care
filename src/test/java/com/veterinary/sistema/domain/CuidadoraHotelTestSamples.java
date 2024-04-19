package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CuidadoraHotelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CuidadoraHotel getCuidadoraHotelSample1() {
        return new CuidadoraHotel()
            .id(1L)
            .nombre("nombre1")
            .direccion("direccion1")
            .telefono("telefono1")
            .servicioOfrecido("servicioOfrecido1");
    }

    public static CuidadoraHotel getCuidadoraHotelSample2() {
        return new CuidadoraHotel()
            .id(2L)
            .nombre("nombre2")
            .direccion("direccion2")
            .telefono("telefono2")
            .servicioOfrecido("servicioOfrecido2");
    }

    public static CuidadoraHotel getCuidadoraHotelRandomSampleGenerator() {
        return new CuidadoraHotel()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .servicioOfrecido(UUID.randomUUID().toString());
    }
}
