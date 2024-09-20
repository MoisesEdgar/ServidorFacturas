package com.ServidorFacturas.partida;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PartidaService {
    @Autowired
    private PartidaRepository repoPartida;

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

        if(partida.getPrecio() <= 0){
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        return repoPartida.save(partida);
    }

}
