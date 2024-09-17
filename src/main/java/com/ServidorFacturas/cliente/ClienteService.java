package com.ServidorFacturas.cliente;

import com.ServidorFacturas.factura.Factura;
import com.ServidorFacturas.partida.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

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

    public Cliente updateCleinte(Cliente cliente, Long id){
        Cliente depDB = repoCliente.findById(id).orElseThrow(()-> new RuntimeException("Cliente no encontrada"));

        if (Objects.nonNull(
                cliente.getCodigo()) && !"".equalsIgnoreCase(cliente.getCodigo())){
            depDB.setCodigo(cliente.getCodigo());
        }

        if (Objects.nonNull(
                cliente.getNombre()) && !"".equalsIgnoreCase(cliente.getNombre())){
            depDB.setNombre(cliente.getNombre());
        }

        if (Objects.nonNull(
                cliente.getTelefono()) && !"".equalsIgnoreCase(cliente.getTelefono())){
            depDB.setTelefono(cliente.getTelefono());
        }

        if (Objects.nonNull(
                cliente.getDireccion()) && !"".equalsIgnoreCase(cliente.getDireccion())){
            depDB.setDireccion(cliente.getDireccion());
        }

        return repoCliente.save(depDB);

    }
}

