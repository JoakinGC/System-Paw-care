package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Estudios} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EstudiosDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

    @NotNull
    private LocalDate fechaCursado;

    @Size(max = 50)
    private String nombreInsituto;

    private Set<VeterinarioDTO> veterinarios = new HashSet<>();

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

    public LocalDate getFechaCursado() {
        return fechaCursado;
    }

    public void setFechaCursado(LocalDate fechaCursado) {
        this.fechaCursado = fechaCursado;
    }

    public String getNombreInsituto() {
        return nombreInsituto;
    }

    public void setNombreInsituto(String nombreInsituto) {
        this.nombreInsituto = nombreInsituto;
    }

    public Set<VeterinarioDTO> getVeterinarios() {
        return veterinarios;
    }

    public void setVeterinarios(Set<VeterinarioDTO> veterinarios) {
        this.veterinarios = veterinarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EstudiosDTO)) {
            return false;
        }

        EstudiosDTO estudiosDTO = (EstudiosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, estudiosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstudiosDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fechaCursado='" + getFechaCursado() + "'" +
            ", nombreInsituto='" + getNombreInsituto() + "'" +
            ", veterinarios=" + getVeterinarios() +
            "}";
    }
}
