package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.CuidadoraHotel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CuidadoraHotelDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombre;

    @Size(max = 50)
    private String direccion;

    @Size(max = 9)
    private String telefono;

    @Size(max = 100)
    private String servicioOfrecido;

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

    public String getServicioOfrecido() {
        return servicioOfrecido;
    }

    public void setServicioOfrecido(String servicioOfrecido) {
        this.servicioOfrecido = servicioOfrecido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CuidadoraHotelDTO)) {
            return false;
        }

        CuidadoraHotelDTO cuidadoraHotelDTO = (CuidadoraHotelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cuidadoraHotelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CuidadoraHotelDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", servicioOfrecido='" + getServicioOfrecido() + "'" +
            "}";
    }
}
