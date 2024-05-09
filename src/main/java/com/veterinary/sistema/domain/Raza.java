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
 * A Raza.
 */
@Entity
@Table(name = "raza")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Raza implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre", length = 20)
    private String nombre;

    @Size(max = 50)
    @Column(name = "nombre_cientifico", length = 50)
    private String nombreCientifico;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "raza")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "historials", "citas", "dueno", "especie", "raza" }, allowSetters = true)
    private Set<Mascota> mascotas = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "razas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "razas", "especies", "terapias", "factores", "historials" }, allowSetters = true)
    private Set<Enfermedad> enfermedads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Raza id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Raza nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCientifico() {
        return this.nombreCientifico;
    }

    public Raza nombreCientifico(String nombreCientifico) {
        this.setNombreCientifico(nombreCientifico);
        return this;
    }

    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
    }

    public Set<Mascota> getMascotas() {
        return this.mascotas;
    }

    public void setMascotas(Set<Mascota> mascotas) {
        if (this.mascotas != null) {
            this.mascotas.forEach(i -> i.setRaza(null));
        }
        if (mascotas != null) {
            mascotas.forEach(i -> i.setRaza(this));
        }
        this.mascotas = mascotas;
    }

    public Raza mascotas(Set<Mascota> mascotas) {
        this.setMascotas(mascotas);
        return this;
    }

    public Raza addMascota(Mascota mascota) {
        this.mascotas.add(mascota);
        mascota.setRaza(this);
        return this;
    }

    public Raza removeMascota(Mascota mascota) {
        this.mascotas.remove(mascota);
        mascota.setRaza(null);
        return this;
    }

    public Set<Enfermedad> getEnfermedads() {
        return this.enfermedads;
    }

    public void setEnfermedads(Set<Enfermedad> enfermedads) {
        if (this.enfermedads != null) {
            this.enfermedads.forEach(i -> i.removeRaza(this));
        }
        if (enfermedads != null) {
            enfermedads.forEach(i -> i.addRaza(this));
        }
        this.enfermedads = enfermedads;
    }

    public Raza enfermedads(Set<Enfermedad> enfermedads) {
        this.setEnfermedads(enfermedads);
        return this;
    }

    public Raza addEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.add(enfermedad);
        enfermedad.getRazas().add(this);
        return this;
    }

    public Raza removeEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.remove(enfermedad);
        enfermedad.getRazas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Raza)) {
            return false;
        }
        return getId() != null && getId().equals(((Raza) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Raza{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", nombreCientifico='" + getNombreCientifico() + "'" +
            "}";
    }
}
