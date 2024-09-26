package com.ServidorFacturas.partida;

import com.ServidorFacturas.factura.Factura;
import com.ServidorFacturas.factura.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class PartidaService {
    @Autowired
    private PartidaRepository repoPartida;

    @Autowired
    private FacturaRepository repoFactura;

    public Partida guardar(Partida partida){

        if(partida.getNombreArticulo() == null || partida.getNombreArticulo().isEmpty()){
            throw new RuntimeException("No se especifico el nombre del articulo");
        }

        if(partida.getCantidad() == null){
            throw new RuntimeException("No se espesifico la cantidad");
        }

        if (partida.getPrecio() == null){
            throw new RuntimeException("No se espesifico el precio");
        }

        if(partida.getCantidad() <= 0){
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        if(partida.getPrecio() <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        return repoPartida.save(partida);

    }

    public Long getIdFactura(Long id){
        Partida partida = repoPartida.findById(id).orElseThrow(()-> new RuntimeException("Partida no encontrada"));
        return partida.getFactura().getId();
    }

}

