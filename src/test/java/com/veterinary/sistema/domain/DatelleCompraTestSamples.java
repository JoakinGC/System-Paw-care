package com.veterinary.sistema.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class DatelleCompraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DatelleCompra getDatelleCompraSample1() {
        return new DatelleCompra().id(1L);
    }

    public static DatelleCompra getDatelleCompraSample2() {
        return new DatelleCompra().id(2L);
    }

    public static DatelleCompra getDatelleCompraRandomSampleGenerator() {
        return new DatelleCompra().id(longCount.incrementAndGet());
    }
}
