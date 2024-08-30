package com.ServidorFacturas.cliente;

import com.ServidorFacturas.factura.Factura;
import com.ServidorFacturas.factura.FacturaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private ClienteService serviceCliente;

    @GetMapping
    public List<ClienteDTO> getAll(){
        List<Cliente> clientes = repoCliente.findAll();
        return clientes.stream()
                .map(cliente -> toDTO(cliente))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteDTO getByID(@PathVariable Long id){
        Cliente entidad = repoCliente.findById(id).orElseThrow(() -> new RuntimeException("no se encontro el Cliente con el id " + id));
        return toDTO(entidad);
    }

    @PostMapping
    public ClienteDTO save(@RequestBody ClienteDTO clienteDTO){
        Cliente cliente = toEntity(clienteDTO);
        Cliente guardada = serviceCliente.guardar(cliente);
        return toDTO(guardada);
    }

    @DeleteMapping("/{id}")
    public String deleteClienteById(@PathVariable Long id){
        repoCliente.deleteById(id);
        return "Se elinimo al cliente con id " + id;
    }


    private ClienteDTO toDTO(Cliente cliente){
        ClienteDTO dto = new ClienteDTO();
        dto.id = cliente.getId();
        dto.codigo = cliente.getCodigo();
        dto.nombre = cliente.getNombre();
        dto.telefono = cliente.getTelefono();
        dto.direccion = cliente.getDireccion();

        if (cliente.getFacturas() != null){
            dto.facturas = new ArrayList<>();

            for(Factura factura : cliente.getFacturas()){
                FacturaDTO fDTO = new FacturaDTO();
                fDTO.id = factura.getId();
                fDTO.folio = factura.getFolio();
                fDTO.fecha_expedicion = factura.getFechaExpedicion();
                fDTO.subtotal = factura.getSubtotal();
                fDTO.total = factura.getTotal();
                fDTO.cliente_id = cliente.getId();

                dto.facturas.add(fDTO);
            }
        }

        return dto;
    }

    private Cliente toEntity(ClienteDTO dto){
        Cliente cliente = new Cliente();
        cliente.setId(dto.id);
        cliente.setCodigo(dto.codigo);
        cliente.setNombre(dto.nombre);
        cliente.setTelefono(dto.telefono);
        cliente.setDireccion(dto.direccion);

        if(dto.facturas != null){

            cliente.setFacturas(new ArrayList<>());

            for (FacturaDTO fDTO : dto.facturas){

                Factura factura = new Factura();
                factura.setId(fDTO.id);
                factura.setFolio(fDTO.folio);
                factura.setFechaExpedicion(fDTO.fecha_expedicion);
                factura.setSubtotal(fDTO.subtotal);
                factura.setTotal(fDTO.total);

                cliente.getFacturas().add(factura);
            }


        }

        return cliente;
    }
}
