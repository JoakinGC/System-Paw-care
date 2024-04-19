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
 * A Mascota.
 */
@Entity
@Table(name = "mascota")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mascota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "n_identificacion_carnet", nullable = false)
    private Integer nIdentificacionCarnet;

    @NotNull
    @Size(max = 255)
    @Column(name = "foto", length = 255, nullable = false)
    private String foto;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mascota")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tratamientos", "medicamentos", "enfermedads", "veterinario", "mascota" }, allowSetters = true)
    private Set<Historial> historials = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_mascota__cita",
        joinColumns = @JoinColumn(name = "mascota_id"),
        inverseJoinColumns = @JoinColumn(name = "cita_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "estetica", "cuidadoraHotel", "veterinario", "mascotas" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "mascotas", "usuario" }, allowSetters = true)
    private Dueno dueno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "mascotas", "enfermedads" }, allowSetters = true)
    private Especie especie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "mascotas", "enfermedads" }, allowSetters = true)
    private Raza raza;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mascota id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getnIdentificacionCarnet() {
        return this.nIdentificacionCarnet;
    }

    public Mascota nIdentificacionCarnet(Integer nIdentificacionCarnet) {
        this.setnIdentificacionCarnet(nIdentificacionCarnet);
        return this;
    }

    public void setnIdentificacionCarnet(Integer nIdentificacionCarnet) {
        this.nIdentificacionCarnet = nIdentificacionCarnet;
    }

    public String getFoto() {
        return this.foto;
    }

    public Mascota foto(String foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Mascota fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Set<Historial> getHistorials() {
        return this.historials;
    }

    public void setHistorials(Set<Historial> historials) {
        if (this.historials != null) {
            this.historials.forEach(i -> i.setMascota(null));
        }
        if (historials != null) {
            historials.forEach(i -> i.setMascota(this));
        }
        this.historials = historials;
    }

    public Mascota historials(Set<Historial> historials) {
        this.setHistorials(historials);
        return this;
    }

    public Mascota addHistorial(Historial historial) {
        this.historials.add(historial);
        historial.setMascota(this);
        return this;
    }

    public Mascota removeHistorial(Historial historial) {
        this.historials.remove(historial);
        historial.setMascota(null);
        return this;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        this.citas = citas;
    }

    public Mascota citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public Mascota addCita(Cita cita) {
        this.citas.add(cita);
        return this;
    }

    public Mascota removeCita(Cita cita) {
        this.citas.remove(cita);
        return this;
    }

    public Dueno getDueno() {
        return this.dueno;
    }

    public void setDueno(Dueno dueno) {
        this.dueno = dueno;
    }

    public Mascota dueno(Dueno dueno) {
        this.setDueno(dueno);
        return this;
    }

    public Especie getEspecie() {
        return this.especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Mascota especie(Especie especie) {
        this.setEspecie(especie);
        return this;
    }

    public Raza getRaza() {
        return this.raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
    }

    public Mascota raza(Raza raza) {
        this.setRaza(raza);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mascota)) {
            return false;
        }
        return getId() != null && getId().equals(((Mascota) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mascota{" +
            "id=" + getId() +
            ", nIdentificacionCarnet=" + getnIdentificacionCarnet() +
            ", foto='" + getFoto() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            "}";
    }
}
