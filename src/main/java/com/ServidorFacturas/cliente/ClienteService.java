package com.ServidorFacturas.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repoCliente;

    public Cliente guardar(Cliente cliente){
        if(cliente.getCodigo() == null || cliente.getCodigo().isEmpty()){
            throw new RuntimeException("No se especifico el codigo del cliente");
        }

        if(cliente.getNombre() == null || cliente.getNombre().isEmpty()){
            throw new RuntimeException("No se especifico el nombre del cliente");
        }

        if(cliente.getTelefono() == null || cliente.getTelefono().isEmpty()){
            throw new RuntimeException("No se especifico el telefono del cliente");
        }

        if(cliente.getDireccion() == null || cliente.getDireccion().isEmpty()){
            throw new RuntimeException("No se especifico la direccion del cliente");
        }

        return repoCliente.save(cliente);
    }
}
