package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Terapia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TerapiaDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

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
        if (!(o instanceof TerapiaDTO)) {
            return false;
        }

        TerapiaDTO terapiaDTO = (TerapiaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, terapiaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerapiaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", enfermedads=" + getEnfermedads() +
            "}";
    }
}
