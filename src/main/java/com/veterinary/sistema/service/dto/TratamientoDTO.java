package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Tratamiento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TratamientoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @Size(max = 200)
    private String notas;

    private HistorialDTO historial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public HistorialDTO getHistorial() {
        return historial;
    }

    public void setHistorial(HistorialDTO historial) {
        this.historial = historial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TratamientoDTO)) {
            return false;
        }

        TratamientoDTO tratamientoDTO = (TratamientoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tratamientoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TratamientoDTO{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", notas='" + getNotas() + "'" +
            ", historial=" + getHistorial() +
            "}";
    }
}
