package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TratamientoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tratamiento getTratamientoSample1() {
        return new Tratamiento().id(1L).notas("notas1");
    }

    public static Tratamiento getTratamientoSample2() {
        return new Tratamiento().id(2L).notas("notas2");
    }

    public static Tratamiento getTratamientoRandomSampleGenerator() {
        return new Tratamiento().id(longCount.incrementAndGet()).notas(UUID.randomUUID().toString());
    }
}
