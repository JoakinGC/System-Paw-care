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
 * Esta clase contiene la informacion es la encargada de manejar
 * el historial de una mascota
 * @autor Joaquin
 */
@Entity
@Table(name = "historial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Historial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_consulta", nullable = false)
    private LocalDate fechaConsulta;

    @NotNull
    @Size(max = 200)
    @Column(name = "diagnostico", length = 200, nullable = false)
    private String diagnostico;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "historial")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "historial" }, allowSetters = true)
    private Set<Tratamiento> tratamientos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_historial__medicamento",
        joinColumns = @JoinColumn(name = "historial_id"),
        inverseJoinColumns = @JoinColumn(name = "medicamento_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "historials" }, allowSetters = true)
    private Set<Medicamento> medicamentos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_historial__enfermedad",
        joinColumns = @JoinColumn(name = "historial_id"),
        inverseJoinColumns = @JoinColumn(name = "enfermedad_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "razas", "especies", "terapias", "factores", "historials" }, allowSetters = true)
    private Set<Enfermedad> enfermedads = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "historials", "citas", "usuario", "estudios" }, allowSetters = true)
    private Veterinario veterinario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "historials", "citas", "dueno", "especie", "raza" }, allowSetters = true)
    private Mascota mascota;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Historial id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaConsulta() {
        return this.fechaConsulta;
    }

    public Historial fechaConsulta(LocalDate fechaConsulta) {
        this.setFechaConsulta(fechaConsulta);
        return this;
    }

    public void setFechaConsulta(LocalDate fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getDiagnostico() {
        return this.diagnostico;
    }

    public Historial diagnostico(String diagnostico) {
        this.setDiagnostico(diagnostico);
        return this;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Set<Tratamiento> getTratamientos() {
        return this.tratamientos;
    }

    public void setTratamientos(Set<Tratamiento> tratamientos) {
        if (this.tratamientos != null) {
            this.tratamientos.forEach(i -> i.setHistorial(null));
        }
        if (tratamientos != null) {
            tratamientos.forEach(i -> i.setHistorial(this));
        }
        this.tratamientos = tratamientos;
    }

    public Historial tratamientos(Set<Tratamiento> tratamientos) {
        this.setTratamientos(tratamientos);
        return this;
    }

    public Historial addTratamiento(Tratamiento tratamiento) {
        this.tratamientos.add(tratamiento);
        tratamiento.setHistorial(this);
        return this;
    }

    public Historial removeTratamiento(Tratamiento tratamiento) {
        this.tratamientos.remove(tratamiento);
        tratamiento.setHistorial(null);
        return this;
    }

    public Set<Medicamento> getMedicamentos() {
        return this.medicamentos;
    }

    public void setMedicamentos(Set<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public Historial medicamentos(Set<Medicamento> medicamentos) {
        this.setMedicamentos(medicamentos);
        return this;
    }

    public Historial addMedicamento(Medicamento medicamento) {
        this.medicamentos.add(medicamento);
        return this;
    }

    public Historial removeMedicamento(Medicamento medicamento) {
        this.medicamentos.remove(medicamento);
        return this;
    }

    public Set<Enfermedad> getEnfermedads() {
        return this.enfermedads;
    }

    public void setEnfermedads(Set<Enfermedad> enfermedads) {
        this.enfermedads = enfermedads;
    }

    public Historial enfermedads(Set<Enfermedad> enfermedads) {
        this.setEnfermedads(enfermedads);
        return this;
    }

    public Historial addEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.add(enfermedad);
        return this;
    }

    public Historial removeEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.remove(enfermedad);
        return this;
    }

    public Veterinario getVeterinario() {
        return this.veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Historial veterinario(Veterinario veterinario) {
        this.setVeterinario(veterinario);
        return this;
    }

    public Mascota getMascota() {
        return this.mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Historial mascota(Mascota mascota) {
        this.setMascota(mascota);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Historial)) {
            return false;
        }
        return getId() != null && getId().equals(((Historial) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Historial{" +
            "id=" + getId() +
            ", fechaConsulta='" + getFechaConsulta() + "'" +
            ", diagnostico='" + getDiagnostico() + "'" +
            "}";
    }
}
