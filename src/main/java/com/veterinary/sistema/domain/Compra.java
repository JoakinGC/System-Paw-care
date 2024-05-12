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
 * A Compra.
 */
@Entity
@Table(name = "compra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;

    @NotNull
    @Column(name = "total", nullable = false)
    private Float total;

    @NotNull
    @Column(name = "entregado", nullable = false)
    private boolean entregado;

    public boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compra")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "compra", "producto" }, allowSetters = true)
    private Set<DatelleCompra> datelleCompras = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "estetica", "veterinario", "dueno", "compras" }, allowSetters = true)
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaCompra() {
        return this.fechaCompra;
    }

    public Compra fechaCompra(LocalDate fechaCompra) {
        this.setFechaCompra(fechaCompra);
        return this;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Float getTotal() {
        return this.total;
    }

    public Compra total(Float total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Set<DatelleCompra> getDatelleCompras() {
        return this.datelleCompras;
    }

    public void setDatelleCompras(Set<DatelleCompra> datelleCompras) {
        if (this.datelleCompras != null) {
            this.datelleCompras.forEach(i -> i.setCompra(null));
        }
        if (datelleCompras != null) {
            datelleCompras.forEach(i -> i.setCompra(this));
        }
        this.datelleCompras = datelleCompras;
    }

    public Compra datelleCompras(Set<DatelleCompra> datelleCompras) {
        this.setDatelleCompras(datelleCompras);
        return this;
    }

    public Compra addDatelleCompra(DatelleCompra datelleCompra) {
        this.datelleCompras.add(datelleCompra);
        datelleCompra.setCompra(this);
        return this;
    }

    public Compra removeDatelleCompra(DatelleCompra datelleCompra) {
        this.datelleCompras.remove(datelleCompra);
        datelleCompra.setCompra(null);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Compra usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compra)) {
            return false;
        }
        return getId() != null && getId().equals(((Compra) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compra{" +
            "id=" + getId() +
            ", fechaCompra='" + getFechaCompra() + "'" +
            ", total=" + getTotal() +
            "}";
    }
}
