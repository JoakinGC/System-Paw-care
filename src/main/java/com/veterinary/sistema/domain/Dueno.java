package com.veterinary.sistema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dueno.
 */
@Entity
@Table(name = "dueno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dueno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre", length = 20)
    private String nombre;

    @Size(max = 20)
    @Column(name = "apellido", length = 20)
    private String apellido;

    @Size(max = 50)
    @Column(name = "direccion", length = 50)
    private String direccion;

    @Size(max = 9)
    @Column(name = "telefono", length = 9)
    private String telefono;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dueno")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "historials", "citas", "dueno", "especie", "raza" }, allowSetters = true)
    private Set<Mascota> mascotas = new HashSet<>();

    @JsonIgnoreProperties(value = { "estetica", "veterinario", "dueno", "compras" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "dueno")
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dueno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Dueno nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Dueno apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Dueno direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Dueno telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Mascota> getMascotas() {
        return this.mascotas;
    }

    public void setMascotas(Set<Mascota> mascotas) {
        if (this.mascotas != null) {
            this.mascotas.forEach(i -> i.setDueno(null));
        }
        if (mascotas != null) {
            mascotas.forEach(i -> i.setDueno(this));
        }
        this.mascotas = mascotas;
    }

    public Dueno mascotas(Set<Mascota> mascotas) {
        this.setMascotas(mascotas);
        return this;
    }

    public Dueno addMascota(Mascota mascota) {
        this.mascotas.add(mascota);
        mascota.setDueno(this);
        return this;
    }

    public Dueno removeMascota(Mascota mascota) {
        this.mascotas.remove(mascota);
        mascota.setDueno(null);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        if (this.usuario != null) {
            this.usuario.setDueno(null);
        }
        if (usuario != null) {
            usuario.setDueno(this);
        }
        this.usuario = usuario;
    }

    public Dueno usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dueno)) {
            return false;
        }
        return getId() != null && getId().equals(((Dueno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dueno{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
