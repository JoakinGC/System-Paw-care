package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MascotaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Mascota getMascotaSample1() {
        return new Mascota().id(1L).nIdentificacionCarnet(1).foto("foto1");
    }

    public static Mascota getMascotaSample2() {
        return new Mascota().id(2L).nIdentificacionCarnet(2).foto("foto2");
    }

    public static Mascota getMascotaRandomSampleGenerator() {
        return new Mascota()
            .id(longCount.incrementAndGet())
            .nIdentificacionCarnet(intCount.incrementAndGet())
            .foto(UUID.randomUUID().toString());
    }
}
