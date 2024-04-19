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
 * A Especie.
 */
@Entity
@Table(name = "especie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Especie implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "especie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "historials", "citas", "dueno", "especie", "raza" }, allowSetters = true)
    private Set<Mascota> mascotas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "especies")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "razas", "especies", "terapias", "factores", "historials" }, allowSetters = true)
    private Set<Enfermedad> enfermedads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Especie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Especie nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCientifico() {
        return this.nombreCientifico;
    }

    public Especie nombreCientifico(String nombreCientifico) {
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
            this.mascotas.forEach(i -> i.setEspecie(null));
        }
        if (mascotas != null) {
            mascotas.forEach(i -> i.setEspecie(this));
        }
        this.mascotas = mascotas;
    }

    public Especie mascotas(Set<Mascota> mascotas) {
        this.setMascotas(mascotas);
        return this;
    }

    public Especie addMascota(Mascota mascota) {
        this.mascotas.add(mascota);
        mascota.setEspecie(this);
        return this;
    }

    public Especie removeMascota(Mascota mascota) {
        this.mascotas.remove(mascota);
        mascota.setEspecie(null);
        return this;
    }

    public Set<Enfermedad> getEnfermedads() {
        return this.enfermedads;
    }

    public void setEnfermedads(Set<Enfermedad> enfermedads) {
        if (this.enfermedads != null) {
            this.enfermedads.forEach(i -> i.removeEspecie(this));
        }
        if (enfermedads != null) {
            enfermedads.forEach(i -> i.addEspecie(this));
        }
        this.enfermedads = enfermedads;
    }

    public Especie enfermedads(Set<Enfermedad> enfermedads) {
        this.setEnfermedads(enfermedads);
        return this;
    }

    public Especie addEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.add(enfermedad);
        enfermedad.getEspecies().add(this);
        return this;
    }

    public Especie removeEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.remove(enfermedad);
        enfermedad.getEspecies().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Especie)) {
            return false;
        }
        return getId() != null && getId().equals(((Especie) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Especie{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", nombreCientifico='" + getNombreCientifico() + "'" +
            "}";
    }
}
