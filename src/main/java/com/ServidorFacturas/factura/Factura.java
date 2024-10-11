package com.ServidorFacturas.factura;

import com.ServidorFacturas.partida.Partida;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;
    private String folio;
    @Column(name = "fecha-expedicion")
    @Temporal(TemporalType.DATE)
    private Date fechaExpedicion;
    private Double subtotal;
    private Double total;
    @Column(name = "cliente-id")
    @Temporal(TemporalType.DATE)
    private Integer clienteId;



    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<Partida> partidas;

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
