package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Factores} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactoresDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

    @Size(max = 40)
    private String tipo;

    @Size(max = 100)
    private String descripcion;

    private Set<EnfermedadDTO> enfermedads = new HashSet<>();

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<EnfermedadDTO> getEnfermedads() {
        return enfermedads;
    }

    public void setEnfermedads(Set<EnfermedadDTO> enfermedads) {
        this.enfermedads = enfermedads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactoresDTO)) {
            return false;
        }

        FactoresDTO factoresDTO = (FactoresDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factoresDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactoresDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", enfermedads=" + getEnfermedads() +
            "}";
    }
}
