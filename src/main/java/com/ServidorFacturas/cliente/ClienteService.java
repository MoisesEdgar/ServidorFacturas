package com.ServidorFacturas.cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repoCliente;

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

        String codigo = crearCodigo();
        cliente.setCodigo(codigo);

        return repoCliente.save(cliente);
    }

    public String crearCodigo(){

            String codigoAnterior = "";
            String codigo = "";

            if(repoCliente.count() == 0){
                codigo = "C-001";
            }else{
                codigoAnterior = repoCliente.findUltimoCodigo().orElse("C-001");

                String[] numeracion = codigoAnterior.split("-");

                Integer cont = Integer.parseInt(numeracion[1]);
                String ceros = "";

                for (int i = String.valueOf(cont).length(); i < 3;) {
                    i++;
                    ceros = ceros + "0";
                }

                codigo = "C-" + ceros + (Integer.parseInt(numeracion[1]) + 1);
            }
            return codigo;

    }

}








