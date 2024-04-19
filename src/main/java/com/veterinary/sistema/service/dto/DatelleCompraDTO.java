package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.DatelleCompra} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DatelleCompraDTO implements Serializable {

    private Long id;

    @NotNull
    private Float cantidad;

    @NotNull
    private Float precioUnitario;

    @NotNull
    private Float totalProducto;

    private CompraDTO compra;

    private ProductoDTO producto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Float getTotalProducto() {
        return totalProducto;
    }

    public void setTotalProducto(Float totalProducto) {
        this.totalProducto = totalProducto;
    }

    public CompraDTO getCompra() {
        return compra;
    }

    public void setCompra(CompraDTO compra) {
        this.compra = compra;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DatelleCompraDTO)) {
            return false;
        }

        DatelleCompraDTO datelleCompraDTO = (DatelleCompraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, datelleCompraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DatelleCompraDTO{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", precioUnitario=" + getPrecioUnitario() +
            ", totalProducto=" + getTotalProducto() +
            ", compra=" + getCompra() +
            ", producto=" + getProducto() +
            "}";
    }
}
