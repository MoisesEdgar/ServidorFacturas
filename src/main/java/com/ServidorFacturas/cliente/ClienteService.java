package com.ServidorFacturas.cliente;
import com.ServidorFacturas.factura.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
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

        validarCodigoCliente(cliente.getCodigo());


        return repoCliente.save(cliente);
    }

    public void validarCodigoCliente(String codigo){

        String codigoFinal = "";
        if (codigo.matches("^C-\\d\\d\\d")) {
            List<Cliente> clientes = repoCliente.findAll();

            for (Cliente cliente : clientes){
                codigoFinal = cliente.getCodigo();
            }

            if(codigoFinal.isEmpty()){
                if(codigo.equalsIgnoreCase("C-001")){
                }else{
                    throw new RuntimeException("El formato del codigo no es valido. La nuemracion debe ser: C-001");
                }
            }else{
                Integer intCodigo = getNumeracion(codigo);
                Integer intCodigoFinal = getNumeracion(codigoFinal);

                if(intCodigo != (intCodigoFinal + 1)){
                    String ceros = "";

                    for (int i = String.valueOf(intCodigoFinal).length(); i < 3;) {
                        i++;
                        ceros = ceros + "0";
                    }

                    throw new RuntimeException("El formato del codigo no es valido. La nuemracion debe ser C-" + ceros + (intCodigoFinal + 1));
                }
            }
        }else{
            throw new RuntimeException("El formato del codigo no es valido debe ser: C-000");
        }


    }

    public Integer getNumeracion(String codigo){
        String[] salto = codigo.split("-");
        return Integer.parseInt(salto[1]);
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








