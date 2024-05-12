package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Compra} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompraDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fechaCompra;

    @NotNull
    private Float total;

    private UsuarioDTO usuario;
    private boolean entregado;

    public boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompraDTO)) {
            return false;
        }

        CompraDTO compraDTO = (CompraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraDTO{" +
            "id=" + getId() +
            ", fechaCompra='" + getFechaCompra() + "'" +
            ", total=" + getTotal() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
