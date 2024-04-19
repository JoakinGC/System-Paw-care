package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Dueno} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DuenoDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

    @Size(max = 20)
    private String apellido;

    @Size(max = 50)
    private String direccion;

    @Size(max = 9)
    private String telefono;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DuenoDTO)) {
            return false;
        }

        DuenoDTO duenoDTO = (DuenoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, duenoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DuenoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
