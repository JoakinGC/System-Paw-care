package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Raza} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RazaDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

    @Size(max = 50)
    private String nombreCientifico;

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

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
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
        if (!(o instanceof RazaDTO)) {
            return false;
        }

        RazaDTO razaDTO = (RazaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, razaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RazaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", nombreCientifico='" + getNombreCientifico() + "'" +
            ", enfermedads=" + getEnfermedads() +
            "}";
    }
}
