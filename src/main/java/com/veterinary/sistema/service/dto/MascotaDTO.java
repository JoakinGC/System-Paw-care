package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Mascota} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MascotaDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer nIdentificacionCarnet;

    @NotNull
    @Size(max = 255)
    private String foto;

    @NotNull
    private LocalDate fechaNacimiento;

    private Set<CitaDTO> citas = new HashSet<>();

    private DuenoDTO dueno;

    private EspecieDTO especie;

    private RazaDTO raza;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getnIdentificacionCarnet() {
        return nIdentificacionCarnet;
    }

    public void setnIdentificacionCarnet(Integer nIdentificacionCarnet) {
        this.nIdentificacionCarnet = nIdentificacionCarnet;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Set<CitaDTO> getCitas() {
        return citas;
    }

    public void setCitas(Set<CitaDTO> citas) {
        this.citas = citas;
    }

    public DuenoDTO getDueno() {
        return dueno;
    }

    public void setDueno(DuenoDTO dueno) {
        this.dueno = dueno;
    }

    public EspecieDTO getEspecie() {
        return especie;
    }

    public void setEspecie(EspecieDTO especie) {
        this.especie = especie;
    }

    public RazaDTO getRaza() {
        return raza;
    }

    public void setRaza(RazaDTO raza) {
        this.raza = raza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MascotaDTO)) {
            return false;
        }

        MascotaDTO mascotaDTO = (MascotaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mascotaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MascotaDTO{" +
            "id=" + getId() +
            ", nIdentificacionCarnet=" + getnIdentificacionCarnet() +
            ", foto='" + getFoto() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", citas=" + getCitas() +
            ", dueno=" + getDueno() +
            ", especie=" + getEspecie() +
            ", raza=" + getRaza() +
            "}";
    }
}
