package com.ServidorFacturas.cliente;

import com.ServidorFacturas.factura.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repoCliente;

    public Cliente guardar(Cliente cliente){
        for (Factura factura : cliente.getFacturas()) {
            factura.setCliente(cliente);
        }

        return repoCliente.save(cliente);
    }
}
