package com.veterinary.sistema.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Historial} entity.
 */
@Schema(description = "Esta clase contiene la informacion es la encargada de manejar\nel historial de una mascota\n@autor Joaquin")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistorialDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fechaConsulta;

    @NotNull
    @Size(max = 200)
    private String diagnostico;

    private Set<MedicamentoDTO> medicamentos = new HashSet<>();

    private Set<EnfermedadDTO> enfermedads = new HashSet<>();

    private VeterinarioDTO veterinario;

    private MascotaDTO mascota;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDate fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Set<MedicamentoDTO> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(Set<MedicamentoDTO> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public Set<EnfermedadDTO> getEnfermedads() {
        return enfermedads;
    }

    public void setEnfermedads(Set<EnfermedadDTO> enfermedads) {
        this.enfermedads = enfermedads;
    }

    public VeterinarioDTO getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(VeterinarioDTO veterinario) {
        this.veterinario = veterinario;
    }

    public MascotaDTO getMascota() {
        return mascota;
    }

    public void setMascota(MascotaDTO mascota) {
        this.mascota = mascota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistorialDTO)) {
            return false;
        }

        HistorialDTO historialDTO = (HistorialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistorialDTO{" +
            "id=" + getId() +
            ", fechaConsulta='" + getFechaConsulta() + "'" +
            ", diagnostico='" + getDiagnostico() + "'" +
            ", medicamentos=" + getMedicamentos() +
            ", enfermedads=" + getEnfermedads() +
            ", veterinario=" + getVeterinario() +
            ", mascota=" + getMascota() +
            "}";
    }
}
