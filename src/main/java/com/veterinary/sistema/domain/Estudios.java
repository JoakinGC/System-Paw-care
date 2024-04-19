package com.veterinary.sistema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Estudios.
 */
@Entity
@Table(name = "estudios")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Estudios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre", length = 20)
    private String nombre;

    @NotNull
    @Column(name = "fecha_cursado", nullable = false)
    private LocalDate fechaCursado;

    @Size(max = 50)
    @Column(name = "nombre_insituto", length = 50)
    private String nombreInsituto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_estudios__veterinario",
        joinColumns = @JoinColumn(name = "estudios_id"),
        inverseJoinColumns = @JoinColumn(name = "veterinario_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "historials", "citas", "usuario", "estudios" }, allowSetters = true)
    private Set<Veterinario> veterinarios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estudios id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Estudios nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaCursado() {
        return this.fechaCursado;
    }

    public Estudios fechaCursado(LocalDate fechaCursado) {
        this.setFechaCursado(fechaCursado);
        return this;
    }

    public void setFechaCursado(LocalDate fechaCursado) {
        this.fechaCursado = fechaCursado;
    }

    public String getNombreInsituto() {
        return this.nombreInsituto;
    }

    public Estudios nombreInsituto(String nombreInsituto) {
        this.setNombreInsituto(nombreInsituto);
        return this;
    }

    public void setNombreInsituto(String nombreInsituto) {
        this.nombreInsituto = nombreInsituto;
    }

    public Set<Veterinario> getVeterinarios() {
        return this.veterinarios;
    }

    public void setVeterinarios(Set<Veterinario> veterinarios) {
        this.veterinarios = veterinarios;
    }

    public Estudios veterinarios(Set<Veterinario> veterinarios) {
        this.setVeterinarios(veterinarios);
        return this;
    }

    public Estudios addVeterinario(Veterinario veterinario) {
        this.veterinarios.add(veterinario);
        return this;
    }

    public Estudios removeVeterinario(Veterinario veterinario) {
        this.veterinarios.remove(veterinario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Estudios)) {
            return false;
        }
        return getId() != null && getId().equals(((Estudios) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estudios{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fechaCursado='" + getFechaCursado() + "'" +
            ", nombreInsituto='" + getNombreInsituto() + "'" +
            "}";
    }
}
