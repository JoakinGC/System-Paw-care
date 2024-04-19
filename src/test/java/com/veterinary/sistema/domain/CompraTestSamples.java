package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CompraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Compra getCompraSample1() {
        return new Compra().id(1L);
    }

    public static Compra getCompraSample2() {
        return new Compra().id(2L);
    }

    public static Compra getCompraRandomSampleGenerator() {
        return new Compra().id(longCount.incrementAndGet());
    }
}
