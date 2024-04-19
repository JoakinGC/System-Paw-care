package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistorialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Historial getHistorialSample1() {
        return new Historial().id(1L).diagnostico("diagnostico1");
    }

    public static Historial getHistorialSample2() {
        return new Historial().id(2L).diagnostico("diagnostico2");
    }

    public static Historial getHistorialRandomSampleGenerator() {
        return new Historial().id(longCount.incrementAndGet()).diagnostico(UUID.randomUUID().toString());
    }
}
