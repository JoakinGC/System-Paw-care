package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Enfermedad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnfermedadDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

    @Size(max = 200)
    private String descripcion;

    private Set<RazaDTO> razas = new HashSet<>();

    private Set<EspecieDTO> especies = new HashSet<>();

    private Set<TerapiaDTO> terapias = new HashSet<>();

    private Set<FactoresDTO> factores = new HashSet<>();

    private Set<HistorialDTO> historials = new HashSet<>();

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

    public Set<RazaDTO> getRazas() {
        return razas;
    }

    public void setRazas(Set<RazaDTO> razas) {
        this.razas = razas;
    }

    public Set<EspecieDTO> getEspecies() {
        return especies;
    }

    public void setEspecies(Set<EspecieDTO> especies) {
        this.especies = especies;
    }

    public Set<TerapiaDTO> getTerapias() {
        return terapias;
    }

    public void setTerapias(Set<TerapiaDTO> terapias) {
        this.terapias = terapias;
    }

    public Set<FactoresDTO> getFactores() {
        return factores;
    }

    public void setFactores(Set<FactoresDTO> factores) {
        this.factores = factores;
    }

    public Set<HistorialDTO> getHistorials() {
        return historials;
    }

    public void setHistorials(Set<HistorialDTO> historials) {
        this.historials = historials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnfermedadDTO)) {
            return false;
        }

        EnfermedadDTO enfermedadDTO = (EnfermedadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enfermedadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnfermedadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", razas=" + getRazas() +
            ", especies=" + getEspecies() +
            ", terapias=" + getTerapias() +
            ", factores=" + getFactores() +
            ", historials=" + getHistorials() +
            "}";
    }
}
