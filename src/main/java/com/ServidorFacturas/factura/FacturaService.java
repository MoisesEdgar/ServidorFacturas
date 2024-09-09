package com.ServidorFacturas.factura;

import com.ServidorFacturas.partida.Partida;
import com.ServidorFacturas.partida.PartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repoFactura;

    public Factura guardar(Factura factura){
        Double subtotal = 0.0;
        Double total = 0.0;

        //FOLIO
        if(factura.getFolio() == null || factura.getFolio().isEmpty()){
            throw new RuntimeException("No se especifico el folio de la factura");
        }

        //FECHA
        factura.setFechaExpedicion(new Date());

        //ID_CLIENTE
        if(factura.getClienteId() == null){
            throw new RuntimeException("No se especifico el id del cliente");
        }

        //PARTIDAS
        for (Partida partida : factura.getPartidas()) {

            //NOMBRE_ARTICULO
            if(partida.getNombreArticulo() == null || partida.getNombreArticulo().isEmpty()){
                throw new RuntimeException("No se especifico el nombre del articulo");
            }

            //CANTIDAD
            Integer cantidad = partida.getCantidad();
            if(cantidad <= 0){
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }

            //PRECIO
            Double precio = partida.getPrecio();
            if(precio < 0.1){
                throw new RuntimeException("El precio debe ser mayor a 0.1");
            }

            subtotal += cantidad * precio;
            //ID_FACTURA
            partida.setFactura(factura);
        }

        total = subtotal + subtotal * 0.16;

        //SUBTOTAL
        factura.setSubtotal(subtotal);

        //TOTAL
        factura.setTotal(total);

        return repoFactura.save(factura);
    }


    //PUT
    public Factura updateFactura(Factura factura, Long FacturaId) {
        Factura depDB = repoFactura.findById(FacturaId).get();

        if (Objects.nonNull(
                factura.getFolio()) && !"".equalsIgnoreCase(factura.getFolio())){
            depDB.setFolio(factura.getFolio());
        }

        if (Objects.nonNull(
                factura.getFechaExpedicion())){
            depDB.setFechaExpedicion(
                    factura.getFechaExpedicion());
        }

        if (Objects.nonNull(
                factura.getSubtotal())){
            depDB.setSubtotal(
                    factura.getSubtotal());
        }

        if (Objects.nonNull(
                factura.getTotal())){
            depDB.setTotal(
                    factura.getTotal());
        }

        if (Objects.nonNull(
                factura.getClienteId())){
            depDB.setClienteId(
                    factura.getClienteId());
        }


        for (Partida partida : factura.getPartidas()) {

            if(Objects.nonNull(
                    partida.getNombreArticulo())){
                partida.setNombreArticulo(
                        partida.getNombreArticulo());
            }

            if(Objects.nonNull(
                    partida.getCantidad())){
                partida.setCantidad(
                        partida.getCantidad());
            }

            if (Objects.nonNull(
                    partida.getPrecio())){
                partida.setPrecio(
                        partida.getPrecio());
            }

        }

        return repoFactura.save(depDB);
    }
}
