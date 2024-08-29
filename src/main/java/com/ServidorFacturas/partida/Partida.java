package com.ServidorFacturas.partida;

import jakarta.persistence.*;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;
    @Column(name = "nombre-articulo")
    @Temporal(TemporalType.DATE)
    private String nombreArticulo;
    private Integer cantidad;
    private Double precio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
