package com.veterinary.sistema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cita.
 */
@Entity
@Table(name = "cita")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "atendido")
    private boolean atendido;

    public boolean getAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }

    @Size(max = 100)
    @Column(name = "motivo", length = 100)
    private String motivo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "citas", "usuario" }, allowSetters = true)
    private Estetica estetica;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "citas" }, allowSetters = true)
    private CuidadoraHotel cuidadoraHotel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "historials", "citas", "usuario", "estudios" }, allowSetters = true)
    private Veterinario veterinario;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "citas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Mascota> mascotas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cita id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Cita hora(LocalTime hora) {
        this.setHora(hora);
        return this;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Cita fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public Cita motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Estetica getEstetica() {
        return this.estetica;
    }

    public void setEstetica(Estetica estetica) {
        this.estetica = estetica;
    }

    public Cita estetica(Estetica estetica) {
        this.setEstetica(estetica);
        return this;
    }

    public CuidadoraHotel getCuidadoraHotel() {
        return this.cuidadoraHotel;
    }

    public void setCuidadoraHotel(CuidadoraHotel cuidadoraHotel) {
        this.cuidadoraHotel = cuidadoraHotel;
    }

    public Cita cuidadoraHotel(CuidadoraHotel cuidadoraHotel) {
        this.setCuidadoraHotel(cuidadoraHotel);
        return this;
    }

    public Veterinario getVeterinario() {
        return this.veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Cita veterinario(Veterinario veterinario) {
        this.setVeterinario(veterinario);
        return this;
    }

    public Set<Mascota> getMascotas() {
        return this.mascotas;
    }

    public void setMascotas(Set<Mascota> mascotas) {
        if (this.mascotas != null) {
            this.mascotas.forEach(i -> i.removeCita(this));
        }
        if (mascotas != null) {
            mascotas.forEach(i -> i.addCita(this));
        }
        this.mascotas = mascotas;
    }

    public Cita mascotas(Set<Mascota> mascotas) {
        this.setMascotas(mascotas);
        return this;
    }

    public Cita addMascota(Mascota mascota) {
        this.mascotas.add(mascota);
        mascota.getCitas().add(this);
        return this;
    }

    public Cita removeMascota(Mascota mascota) {
        this.mascotas.remove(mascota);
        mascota.getCitas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cita)) {
            return false;
        }
        return getId() != null && getId().equals(((Cita) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cita{" +
            "id=" + getId() +
            ", hora='" + getHora() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", motivo='" + getMotivo() + "'" +
            "}";
    }
}
