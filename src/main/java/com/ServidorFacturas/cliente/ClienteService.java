package com.ServidorFacturas.cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClienteService {

    @Autowired
    ClienteRepository repoCliente;

    public Cliente guardar(Cliente cliente){

        if(cliente.getNombre() == null || cliente.getNombre().isEmpty()){
            throw new RuntimeException("No se especifico el nombre del cliente");
        }

        Cliente existenciaCliente = repoCliente.findByNombre(cliente.getNombre()).orElse(null);
        if (existenciaCliente != null){
            throw new RuntimeException("El nombre del cliente ya esta registrado");
        }

        if(cliente.getTelefono() == null || cliente.getTelefono().isEmpty()){
            throw new RuntimeException("No se especifico el telefono del cliente");
        }

        if(cliente.getDireccion() == null || cliente.getDireccion().isEmpty()){
            throw new RuntimeException("No se especifico la direccion del cliente");
        }

        String codigo = crearCodigo(cliente.getNombre());
        cliente.setCodigo(codigo);

        return repoCliente.save(cliente);
    }

    public String crearCodigo(String nombre){
//
//        Cliente clienteAnterior = repoCliente.findByNombre(nombre).orElse(null);
//
//        String codigoAnterior = "";
//        String codigo = "";
//
//        Integer tabla = repoCliente.tamanoTabla();
//
//        if(tabla == 0){
//            codigo = "C-001";
//        }else{
//            codigoAnterior = clienteAnterior.getCodigo();
//
//            String[] salto = codigoAnterior.split("-");
//
//            Integer cont = Integer.parseInt(salto[1]);
//            String ceros = "";
//
//            for (int i = String.valueOf(cont).length(); i < 3;) {
//                i++;
//                ceros = ceros + "0";
//            }
//
//            codigo = "C-" + ceros + (Integer.parseInt(salto[1]) + 1);
//        }
//        return codigo;

        List<Cliente> clientes = repoCliente.findAll();

        String codigoAnterior = "";
        String codigo = "";

        if(clientes.isEmpty()){
            codigo = "C-001";
        }else{
            Integer ultimo = clientes.size();
            Cliente cliente = clientes.get(ultimo-1);

            codigoAnterior = cliente.getCodigo();

            String[] salto = codigoAnterior.split("-");

            Integer cont = Integer.parseInt(salto[1]);
            String ceros = "";

            for (int i = String.valueOf(cont).length(); i < 3;) {
                i++;
                ceros = ceros + "0";
            }

            codigo = "C-" + ceros + (Integer.parseInt(salto[1]) + 1);
        }
        return codigo;

    }

}








