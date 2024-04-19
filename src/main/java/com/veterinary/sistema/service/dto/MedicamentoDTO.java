package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Medicamento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicamentoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombre;

    private String descripcion;

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
        if (!(o instanceof MedicamentoDTO)) {
            return false;
        }

        MedicamentoDTO medicamentoDTO = (MedicamentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicamentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicamentoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", historials=" + getHistorials() +
            "}";
    }
}
