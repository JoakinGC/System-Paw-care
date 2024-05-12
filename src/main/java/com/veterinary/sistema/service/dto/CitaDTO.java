package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Cita} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitaDTO implements Serializable {

    private Long id;

    private LocalTime hora;

    private LocalDate fecha;

    @Size(max = 100)
    private String motivo;

    private EsteticaDTO estetica;

    private boolean atendido;

    public boolean getAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }

    private CuidadoraHotelDTO cuidadoraHotel;

    private VeterinarioDTO veterinario;

    private Set<MascotaDTO> mascotas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EsteticaDTO getEstetica() {
        return estetica;
    }

    public void setEstetica(EsteticaDTO estetica) {
        this.estetica = estetica;
    }

    public CuidadoraHotelDTO getCuidadoraHotel() {
        return cuidadoraHotel;
    }

    public void setCuidadoraHotel(CuidadoraHotelDTO cuidadoraHotel) {
        this.cuidadoraHotel = cuidadoraHotel;
    }

    public VeterinarioDTO getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(VeterinarioDTO veterinario) {
        this.veterinario = veterinario;
    }

    public Set<MascotaDTO> getMascotas() {
        return mascotas;
    }

    public void setMascotas(Set<MascotaDTO> mascotas) {
        this.mascotas = mascotas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitaDTO)) {
            return false;
        }

        CitaDTO citaDTO = (CitaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, citaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaDTO{" +
            "id=" + getId() +
            ", hora='" + getHora() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", motivo='" + getMotivo() + "'" +
            ", estetica=" + getEstetica() +
            ", cuidadoraHotel=" + getCuidadoraHotel() +
            ", veterinario=" + getVeterinario() +
            ", mascotas=" + getMascotas() +
            "}";
    }
}
