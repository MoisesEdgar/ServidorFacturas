package com.ServidorFacturas.cliente;

import com.ServidorFacturas.factura.FacturaDTO;
import com.ServidorFacturas.partida.PartidaDTO;

import java.util.List;

public class ClienteDTO {

    public Long id;
    public String codigo;
    public String nombre;
    public String telefono;
    public String direccion;
    public List<FacturaDTO> facturas;
}
