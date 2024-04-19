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
 * A CuidadoraHotel.
 */
@Entity
@Table(name = "cuidadora_hotel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CuidadoraHotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre", length = 20)
    private String nombre;

    @Size(max = 50)
    @Column(name = "direccion", length = 50)
    private String direccion;

    @Size(max = 9)
    @Column(name = "telefono", length = 9)
    private String telefono;

    @Size(max = 100)
    @Column(name = "servicio_ofrecido", length = 100)
    private String servicioOfrecido;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cuidadoraHotel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "estetica", "cuidadoraHotel", "veterinario", "mascotas" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CuidadoraHotel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public CuidadoraHotel nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public CuidadoraHotel direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public CuidadoraHotel telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getServicioOfrecido() {
        return this.servicioOfrecido;
    }

    public CuidadoraHotel servicioOfrecido(String servicioOfrecido) {
        this.setServicioOfrecido(servicioOfrecido);
        return this;
    }

    public void setServicioOfrecido(String servicioOfrecido) {
        this.servicioOfrecido = servicioOfrecido;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        if (this.citas != null) {
            this.citas.forEach(i -> i.setCuidadoraHotel(null));
        }
        if (citas != null) {
            citas.forEach(i -> i.setCuidadoraHotel(this));
        }
        this.citas = citas;
    }

    public CuidadoraHotel citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public CuidadoraHotel addCita(Cita cita) {
        this.citas.add(cita);
        cita.setCuidadoraHotel(this);
        return this;
    }

    public CuidadoraHotel removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setCuidadoraHotel(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CuidadoraHotel)) {
            return false;
        }
        return getId() != null && getId().equals(((CuidadoraHotel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CuidadoraHotel{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", servicioOfrecido='" + getServicioOfrecido() + "'" +
            "}";
    }
}
