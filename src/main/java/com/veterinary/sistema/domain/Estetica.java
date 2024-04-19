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
 * A Estetica.
 */
@Entity
@Table(name = "estetica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Estetica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre", length = 20)
    private String nombre;

    @Size(max = 50)
    @Column(name = "direcion", length = 50)
    private String direcion;

    @Size(max = 9)
    @Column(name = "telefono", length = 9)
    private String telefono;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estetica")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "estetica", "cuidadoraHotel", "veterinario", "mascotas" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    @JsonIgnoreProperties(value = { "estetica", "veterinario", "dueno", "compras" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "estetica")
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estetica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Estetica nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirecion() {
        return this.direcion;
    }

    public Estetica direcion(String direcion) {
        this.setDirecion(direcion);
        return this;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Estetica telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        if (this.citas != null) {
            this.citas.forEach(i -> i.setEstetica(null));
        }
        if (citas != null) {
            citas.forEach(i -> i.setEstetica(this));
        }
        this.citas = citas;
    }

    public Estetica citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public Estetica addCita(Cita cita) {
        this.citas.add(cita);
        cita.setEstetica(this);
        return this;
    }

    public Estetica removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setEstetica(null);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        if (this.usuario != null) {
            this.usuario.setEstetica(null);
        }
        if (usuario != null) {
            usuario.setEstetica(this);
        }
        this.usuario = usuario;
    }

    public Estetica usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Estetica)) {
            return false;
        }
        return getId() != null && getId().equals(((Estetica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estetica{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direcion='" + getDirecion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
