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
 * A Factores.
 */
@Entity
@Table(name = "factores")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Factores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre", length = 20)
    private String nombre;

    @Size(max = 40)
    @Column(name = "tipo", length = 40)
    private String tipo;

    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "factores")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "razas", "especies", "terapias", "factores", "historials" }, allowSetters = true)
    private Set<Enfermedad> enfermedads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factores id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Factores nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Factores tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Factores descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Enfermedad> getEnfermedads() {
        return this.enfermedads;
    }

    public void setEnfermedads(Set<Enfermedad> enfermedads) {
        if (this.enfermedads != null) {
            this.enfermedads.forEach(i -> i.removeFactores(this));
        }
        if (enfermedads != null) {
            enfermedads.forEach(i -> i.addFactores(this));
        }
        this.enfermedads = enfermedads;
    }

    public Factores enfermedads(Set<Enfermedad> enfermedads) {
        this.setEnfermedads(enfermedads);
        return this;
    }

    public Factores addEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.add(enfermedad);
        enfermedad.getFactores().add(this);
        return this;
    }

    public Factores removeEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.remove(enfermedad);
        enfermedad.getFactores().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factores)) {
            return false;
        }
        return getId() != null && getId().equals(((Factores) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factores{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
