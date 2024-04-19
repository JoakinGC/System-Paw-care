package com.veterinary.sistema.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.veterinary.sistema.domain.Usuario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String nombreUsuario;

    @Size(max = 255)
    private String rol;

    private EsteticaDTO estetica;

    private VeterinarioDTO veterinario;

    private DuenoDTO dueno;

    private UserDTO userDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public EsteticaDTO getEstetica() {
        return estetica;
    }

    public void setEstetica(EsteticaDTO estetica) {
        this.estetica = estetica;
    }

    public VeterinarioDTO getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(VeterinarioDTO veterinario) {
        this.veterinario = veterinario;
    }

    public DuenoDTO getDueno() {
        return dueno;
    }

    public void setDueno(DuenoDTO dueno) {
        this.dueno = dueno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioDTO)) {
            return false;
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usuarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioDTO{" +
            "id=" + getId() +
            ", nombreUsuario='" + getNombreUsuario() + "'" +
            ", rol='" + getRol() + "'" +
            ", estetica=" + getEstetica() +
            ", veterinario=" + getVeterinario() +
            ", dueno=" + getDueno() +
            "}";
    }
}
