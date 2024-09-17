package com.ServidorFacturas.cliente;

import com.ServidorFacturas.factura.Factura;
import com.ServidorFacturas.factura.FacturaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        Cliente entidad = repoCliente.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el Cliente con el id " + id));
        return toDTO(entidad);
    }

    @PostMapping
    public ClienteDTO save(@RequestBody ClienteDTO clienteDTO){
        Cliente cliente = toEntity(clienteDTO);
        Cliente guardada = serviceCliente.guardar(cliente);
        return toDTO(guardada);
    }

    @PutMapping("/{id}")
    public ClienteDTO updateCliente(@RequestBody ClienteDTO clienteDTO, @PathVariable Long id){
        Cliente cliente = toEntity(clienteDTO);
        Cliente modificada = serviceCliente.updateCleinte(cliente ,id);
        return toDTO(modificada);
    }

  /*  @DeleteMapping("/{id}")
    public String deleteClienteById(@PathVariable Long id){
        repoCliente.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el Cliente con el id " + id));
        repoCliente.deleteById(id);
        return "Se elinimo al cliente con id " + id;
    }

*/

    private ClienteDTO toDTO(Cliente cliente){
        ClienteDTO dto = new ClienteDTO();
        dto.id = cliente.getId();
        dto.codigo = cliente.getCodigo();
        dto.nombre = cliente.getNombre();
        dto.telefono = cliente.getTelefono();
        dto.direccion = cliente.getDireccion();

        return dto;
    }

    private Cliente toEntity(ClienteDTO dto){
        Cliente cliente = new Cliente();
        cliente.setId(dto.id);
        cliente.setCodigo(dto.codigo);
        cliente.setNombre(dto.nombre);
        cliente.setTelefono(dto.telefono);
        cliente.setDireccion(dto.direccion);

        return cliente;
    }
}
