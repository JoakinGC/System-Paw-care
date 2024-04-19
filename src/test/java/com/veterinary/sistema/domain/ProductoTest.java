package com.veterinary.sistema.domain;

import static com.veterinary.sistema.domain.DatelleCompraTestSamples.*;
import static com.veterinary.sistema.domain.ProductoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.veterinary.sistema.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producto.class);
        Producto producto1 = getProductoSample1();
        Producto producto2 = new Producto();
        assertThat(producto1).isNotEqualTo(producto2);

        producto2.setId(producto1.getId());
        assertThat(producto1).isEqualTo(producto2);

        producto2 = getProductoSample2();
        assertThat(producto1).isNotEqualTo(producto2);
    }

    @Test
    void datelleCompraTest() throws Exception {
        Producto producto = getProductoRandomSampleGenerator();
        DatelleCompra datelleCompraBack = getDatelleCompraRandomSampleGenerator();

        producto.addDatelleCompra(datelleCompraBack);
        assertThat(producto.getDatelleCompras()).containsOnly(datelleCompraBack);
        assertThat(datelleCompraBack.getProducto()).isEqualTo(producto);

        producto.removeDatelleCompra(datelleCompraBack);
        assertThat(producto.getDatelleCompras()).doesNotContain(datelleCompraBack);
        assertThat(datelleCompraBack.getProducto()).isNull();

        producto.datelleCompras(new HashSet<>(Set.of(datelleCompraBack)));
        assertThat(producto.getDatelleCompras()).containsOnly(datelleCompraBack);
        assertThat(datelleCompraBack.getProducto()).isEqualTo(producto);

        producto.setDatelleCompras(new HashSet<>());
        assertThat(producto.getDatelleCompras()).doesNotContain(datelleCompraBack);
        assertThat(datelleCompraBack.getProducto()).isNull();
    }
}
