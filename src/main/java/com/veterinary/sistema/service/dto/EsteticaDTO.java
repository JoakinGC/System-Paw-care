package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Estetica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EsteticaDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

    @Size(max = 50)
    private String direcion;

    @Size(max = 9)
    private String telefono;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EsteticaDTO)) {
            return false;
        }

        EsteticaDTO esteticaDTO = (EsteticaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, esteticaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EsteticaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direcion='" + getDirecion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
