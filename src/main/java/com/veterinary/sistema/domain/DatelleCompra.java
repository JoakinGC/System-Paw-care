package com.veterinary.sistema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DatelleCompra.
 */
@Entity
@Table(name = "datelle_compra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DatelleCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Float cantidad;

    @NotNull
    @Column(name = "precio_unitario", nullable = false)
    private Float precioUnitario;

    @NotNull
    @Column(name = "total_producto", nullable = false)
    private Float totalProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "datelleCompras", "usuario" }, allowSetters = true)
    private Compra compra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "datelleCompras" }, allowSetters = true)
    private Producto producto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DatelleCompra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCantidad() {
        return this.cantidad;
    }

    public DatelleCompra cantidad(Float cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioUnitario() {
        return this.precioUnitario;
    }

    public DatelleCompra precioUnitario(Float precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Float getTotalProducto() {
        return this.totalProducto;
    }

    public DatelleCompra totalProducto(Float totalProducto) {
        this.setTotalProducto(totalProducto);
        return this;
    }

    public void setTotalProducto(Float totalProducto) {
        this.totalProducto = totalProducto;
    }

    public Compra getCompra() {
        return this.compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public DatelleCompra compra(Compra compra) {
        this.setCompra(compra);
        return this;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public DatelleCompra producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DatelleCompra)) {
            return false;
        }
        return getId() != null && getId().equals(((DatelleCompra) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DatelleCompra{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", precioUnitario=" + getPrecioUnitario() +
            ", totalProducto=" + getTotalProducto() +
            "}";
    }
}
