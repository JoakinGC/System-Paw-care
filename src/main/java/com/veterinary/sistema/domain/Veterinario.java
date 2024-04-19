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
 * A Veterinario.
 */
@Entity
@Table(name = "veterinario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Veterinario implements Serializable {

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

    @Size(max = 20)
    @Column(name = "especilidad", length = 20)
    private String especilidad;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "veterinario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tratamientos", "medicamentos", "enfermedads", "veterinario", "mascota" }, allowSetters = true)
    private Set<Historial> historials = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "veterinario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "estetica", "cuidadoraHotel", "veterinario", "mascotas" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    @JsonIgnoreProperties(value = { "estetica", "veterinario", "dueno", "compras" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "veterinario")
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "veterinarios")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "veterinarios" }, allowSetters = true)
    private Set<Estudios> estudios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Veterinario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Veterinario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Veterinario apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Veterinario direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Veterinario telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEspecilidad() {
        return this.especilidad;
    }

    public Veterinario especilidad(String especilidad) {
        this.setEspecilidad(especilidad);
        return this;
    }

    public void setEspecilidad(String especilidad) {
        this.especilidad = especilidad;
    }

    public Set<Historial> getHistorials() {
        return this.historials;
    }

    public void setHistorials(Set<Historial> historials) {
        if (this.historials != null) {
            this.historials.forEach(i -> i.setVeterinario(null));
        }
        if (historials != null) {
            historials.forEach(i -> i.setVeterinario(this));
        }
        this.historials = historials;
    }

    public Veterinario historials(Set<Historial> historials) {
        this.setHistorials(historials);
        return this;
    }

    public Veterinario addHistorial(Historial historial) {
        this.historials.add(historial);
        historial.setVeterinario(this);
        return this;
    }

    public Veterinario removeHistorial(Historial historial) {
        this.historials.remove(historial);
        historial.setVeterinario(null);
        return this;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        if (this.citas != null) {
            this.citas.forEach(i -> i.setVeterinario(null));
        }
        if (citas != null) {
            citas.forEach(i -> i.setVeterinario(this));
        }
        this.citas = citas;
    }

    public Veterinario citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public Veterinario addCita(Cita cita) {
        this.citas.add(cita);
        cita.setVeterinario(this);
        return this;
    }

    public Veterinario removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setVeterinario(null);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        if (this.usuario != null) {
            this.usuario.setVeterinario(null);
        }
        if (usuario != null) {
            usuario.setVeterinario(this);
        }
        this.usuario = usuario;
    }

    public Veterinario usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Set<Estudios> getEstudios() {
        return this.estudios;
    }

    public void setEstudios(Set<Estudios> estudios) {
        if (this.estudios != null) {
            this.estudios.forEach(i -> i.removeVeterinario(this));
        }
        if (estudios != null) {
            estudios.forEach(i -> i.addVeterinario(this));
        }
        this.estudios = estudios;
    }

    public Veterinario estudios(Set<Estudios> estudios) {
        this.setEstudios(estudios);
        return this;
    }

    public Veterinario addEstudios(Estudios estudios) {
        this.estudios.add(estudios);
        estudios.getVeterinarios().add(this);
        return this;
    }

    public Veterinario removeEstudios(Estudios estudios) {
        this.estudios.remove(estudios);
        estudios.getVeterinarios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Veterinario)) {
            return false;
        }
        return getId() != null && getId().equals(((Veterinario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Veterinario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", especilidad='" + getEspecilidad() + "'" +
            "}";
    }
}
