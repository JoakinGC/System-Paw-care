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
 * A Producto.
 */
@Entity
@Table(name = "producto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private Float nombre;

    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Size(max = 200)
    @Column(name = "src", length = 200)
    private String ruta;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "compra", "producto" }, allowSetters = true)
    private Set<DatelleCompra> datelleCompras = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Long getId() {
        return this.id;
    }

    public Producto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNombre() {
        return this.nombre;
    }

    public Producto nombre(Float nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(Float nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Producto descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<DatelleCompra> getDatelleCompras() {
        return this.datelleCompras;
    }

    public void setDatelleCompras(Set<DatelleCompra> datelleCompras) {
        if (this.datelleCompras != null) {
            this.datelleCompras.forEach(i -> i.setProducto(null));
        }
        if (datelleCompras != null) {
            datelleCompras.forEach(i -> i.setProducto(this));
        }
        this.datelleCompras = datelleCompras;
    }

    public Producto datelleCompras(Set<DatelleCompra> datelleCompras) {
        this.setDatelleCompras(datelleCompras);
        return this;
    }

    public Producto addDatelleCompra(DatelleCompra datelleCompra) {
        this.datelleCompras.add(datelleCompra);
        datelleCompra.setProducto(this);
        return this;
    }

    public Producto removeDatelleCompra(DatelleCompra datelleCompra) {
        this.datelleCompras.remove(datelleCompra);
        datelleCompra.setProducto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return getId() != null && getId().equals(((Producto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", nombre=" + getNombre() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
