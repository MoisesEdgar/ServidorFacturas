package com.ServidorFacturas.factura;

import com.ServidorFacturas.cliente.ClienteDTO;
import com.ServidorFacturas.partida.PartidaDTO;

import java.util.Date;
import java.util.List;

public class FacturaDTO {
    public Long id;
    public String folio;
    public Date fecha_expedicion;
    public Double subtotal;
    public Double total;
    public ClienteDTO cliente;
    public List<PartidaDTO> partidas;
}
