package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Veterinario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VeterinarioDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

    @Size(max = 20)
    private String apellido;

    @Size(max = 50)
    private String direccion;

    @Size(max = 9)
    private String telefono;

    @Size(max = 20)
    private String especilidad;

    private Set<EstudiosDTO> estudios = new HashSet<>();

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEspecilidad() {
        return especilidad;
    }

    public void setEspecilidad(String especilidad) {
        this.especilidad = especilidad;
    }

    public Set<EstudiosDTO> getEstudios() {
        return estudios;
    }

    public void setEstudios(Set<EstudiosDTO> estudios) {
        this.estudios = estudios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VeterinarioDTO)) {
            return false;
        }

        VeterinarioDTO veterinarioDTO = (VeterinarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, veterinarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VeterinarioDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", especilidad='" + getEspecilidad() + "'" +
            ", estudios=" + getEstudios() +
            "}";
    }
}
