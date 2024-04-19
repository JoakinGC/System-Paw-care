package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.CompraTestSamples.*;
import static com.veterinary.sistema.domain.DatelleCompraTestSamples.*;
import static com.veterinary.sistema.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DatelleCompraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatelleCompra.class);
        DatelleCompra datelleCompra1 = getDatelleCompraSample1();
        DatelleCompra datelleCompra2 = new DatelleCompra();
        assertThat(datelleCompra1).isNotEqualTo(datelleCompra2);

        datelleCompra2.setId(datelleCompra1.getId());
        assertThat(datelleCompra1).isEqualTo(datelleCompra2);

        datelleCompra2 = getDatelleCompraSample2();
        assertThat(datelleCompra1).isNotEqualTo(datelleCompra2);
    }

    @Test
    void compraTest() throws Exception {
        DatelleCompra datelleCompra = getDatelleCompraRandomSampleGenerator();
        Compra compraBack = getCompraRandomSampleGenerator();

        datelleCompra.setCompra(compraBack);
        assertThat(datelleCompra.getCompra()).isEqualTo(compraBack);

        datelleCompra.compra(null);
        assertThat(datelleCompra.getCompra()).isNull();
    }

    @Test
    void productoTest() throws Exception {
        DatelleCompra datelleCompra = getDatelleCompraRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        datelleCompra.setProducto(productoBack);
        assertThat(datelleCompra.getProducto()).isEqualTo(productoBack);

        datelleCompra.producto(null);
        assertThat(datelleCompra.getProducto()).isNull();
    }
}
