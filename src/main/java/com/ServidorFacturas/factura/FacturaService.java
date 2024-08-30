package com.ServidorFacturas.factura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repoFactura;

    public Factura guardar(Factura factura){

        factura.setFechaExpedicion(new Date());
        Double totalFactura = 0.0;
        factura.setTotal(totalFactura);

        return repoFactura.save(factura);

    }
}
