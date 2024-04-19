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
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre_usuario", length = 20)
    private String nombreUsuario;

    @Size(max = 255)
    @Column(name = "rol", length = 255)
    private String rol;

    @JsonIgnoreProperties(value = { "citas", "usuario" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Estetica estetica;

    @JsonIgnoreProperties(value = { "historials", "citas", "usuario", "estudios" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Veterinario veterinario;

    @JsonIgnoreProperties(value = { "mascotas", "usuario" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Dueno dueno;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "datelleCompras", "usuario" }, allowSetters = true)
    private Set<Compra> compras = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return this.nombreUsuario;
    }

    public Usuario nombreUsuario(String nombreUsuario) {
        this.setNombreUsuario(nombreUsuario);
        return this;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return this.rol;
    }

    public Usuario rol(String rol) {
        this.setRol(rol);
        return this;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Estetica getEstetica() {
        return this.estetica;
    }

    public void setEstetica(Estetica estetica) {
        this.estetica = estetica;
    }

    public Usuario estetica(Estetica estetica) {
        this.setEstetica(estetica);
        return this;
    }

    public Veterinario getVeterinario() {
        return this.veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Usuario veterinario(Veterinario veterinario) {
        this.setVeterinario(veterinario);
        return this;
    }

    public Dueno getDueno() {
        return this.dueno;
    }

    public void setDueno(Dueno dueno) {
        this.dueno = dueno;
    }

    public Usuario dueno(Dueno dueno) {
        this.setDueno(dueno);
        return this;
    }

    public Set<Compra> getCompras() {
        return this.compras;
    }

    public void setCompras(Set<Compra> compras) {
        if (this.compras != null) {
            this.compras.forEach(i -> i.setUsuario(null));
        }
        if (compras != null) {
            compras.forEach(i -> i.setUsuario(this));
        }
        this.compras = compras;
    }

    public Usuario compras(Set<Compra> compras) {
        this.setCompras(compras);
        return this;
    }

    public Usuario addCompra(Compra compra) {
        this.compras.add(compra);
        compra.setUsuario(this);
        return this;
    }

    public Usuario removeCompra(Compra compra) {
        this.compras.remove(compra);
        compra.setUsuario(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return getId() != null && getId().equals(((Usuario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nombreUsuario='" + getNombreUsuario() + "'" +
            ", rol='" + getRol() + "'" +
            "}";
    }
}
