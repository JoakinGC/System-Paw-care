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
 * A Enfermedad.
 */
@Entity
@Table(name = "enfermedad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enfermedad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre", length = 20)
    private String nombre;

    @Size(max = 200)
    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_enfermedad__raza",
        joinColumns = @JoinColumn(name = "enfermedad_id"),
        inverseJoinColumns = @JoinColumn(name = "raza_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mascotas", "enfermedads" }, allowSetters = true)
    private Set<Raza> razas = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_enfermedad__especie",
        joinColumns = @JoinColumn(name = "enfermedad_id"),
        inverseJoinColumns = @JoinColumn(name = "especie_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mascotas", "enfermedads" }, allowSetters = true)
    private Set<Especie> especies = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_enfermedad__terapia",
        joinColumns = @JoinColumn(name = "enfermedad_id"),
        inverseJoinColumns = @JoinColumn(name = "terapia_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "enfermedads" }, allowSetters = true)
    private Set<Terapia> terapias = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_enfermedad__factores",
        joinColumns = @JoinColumn(name = "enfermedad_id"),
        inverseJoinColumns = @JoinColumn(name = "factores_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "enfermedads" }, allowSetters = true)
    private Set<Factores> factores = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "enfermedads")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tratamientos", "medicamentos", "enfermedads", "veterinario", "mascota" }, allowSetters = true)
    private Set<Historial> historials = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enfermedad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Enfermedad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Enfermedad descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Raza> getRazas() {
        return this.razas;
    }

    public void setRazas(Set<Raza> razas) {
        this.razas = razas;
    }

    public Enfermedad razas(Set<Raza> razas) {
        this.setRazas(razas);
        return this;
    }

    public Enfermedad addRaza(Raza raza) {
        this.razas.add(raza);
        return this;
    }

    public Enfermedad removeRaza(Raza raza) {
        this.razas.remove(raza);
        return this;
    }

    public Set<Especie> getEspecies() {
        return this.especies;
    }

    public void setEspecies(Set<Especie> especies) {
        this.especies = especies;
    }

    public Enfermedad especies(Set<Especie> especies) {
        this.setEspecies(especies);
        return this;
    }

    public Enfermedad addEspecie(Especie especie) {
        this.especies.add(especie);
        return this;
    }

    public Enfermedad removeEspecie(Especie especie) {
        this.especies.remove(especie);
        return this;
    }

    public Set<Terapia> getTerapias() {
        return this.terapias;
    }

    public void setTerapias(Set<Terapia> terapias) {
        this.terapias = terapias;
    }

    public Enfermedad terapias(Set<Terapia> terapias) {
        this.setTerapias(terapias);
        return this;
    }

    public Enfermedad addTerapia(Terapia terapia) {
        this.terapias.add(terapia);
        return this;
    }

    public Enfermedad removeTerapia(Terapia terapia) {
        this.terapias.remove(terapia);
        return this;
    }

    public Set<Factores> getFactores() {
        return this.factores;
    }

    public void setFactores(Set<Factores> factores) {
        this.factores = factores;
    }

    public Enfermedad factores(Set<Factores> factores) {
        this.setFactores(factores);
        return this;
    }

    public Enfermedad addFactores(Factores factores) {
        this.factores.add(factores);
        return this;
    }

    public Enfermedad removeFactores(Factores factores) {
        this.factores.remove(factores);
        return this;
    }

    public Set<Historial> getHistorials() {
        return this.historials;
    }

    public void setHistorials(Set<Historial> historials) {
        if (this.historials != null) {
            this.historials.forEach(i -> i.removeEnfermedad(this));
        }
        if (historials != null) {
            historials.forEach(i -> i.addEnfermedad(this));
        }
        this.historials = historials;
    }

    public Enfermedad historials(Set<Historial> historials) {
        this.setHistorials(historials);
        return this;
    }

    public Enfermedad addHistorial(Historial historial) {
        this.historials.add(historial);
        historial.getEnfermedads().add(this);
        return this;
    }

    public Enfermedad removeHistorial(Historial historial) {
        this.historials.remove(historial);
        historial.getEnfermedads().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enfermedad)) {
            return false;
        }
        return getId() != null && getId().equals(((Enfermedad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enfermedad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
