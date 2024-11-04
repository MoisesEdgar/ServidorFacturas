package com.ServidorFacturas.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private ClienteService serviceCliente;

    @GetMapping("/{id}")
    public ClienteDTO getById(@PathVariable Long id){
        Cliente cliente = repoCliente.findById(id).orElseThrow(() -> new RuntimeException("No se encontro ningun cliente con el id : " + id));
        return toDTO(cliente);
    }


    @GetMapping("/nombre")
    public ResponseEntity<ClienteDTO> getByNombre(@RequestParam String nombre){
        Cliente cliente = repoCliente.findByNombre(nombre).orElse(null);
        if (cliente == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(cliente));

    }

    @GetMapping("/codigo")
    public ResponseEntity<ClienteDTO> getByCodigo(@RequestParam String codigo){
        Cliente cliente = repoCliente.findByCodigo(codigo).orElse(null);
        if (cliente == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(cliente));
    }

//    @GetMapping
//    public List<ClienteDTO> getAll(){
//        List<Cliente> clientes = repoCliente.findAll();
//        return clientes.stream().
//                map(cliente -> toDTO(cliente)).
//                collect(Collectors.toList());
//    }

    @PostMapping
    public ClienteDTO save(@RequestBody ClienteDTO clienteDTO){
        Cliente cliente = toEntity(clienteDTO);
        Cliente guardada = serviceCliente.guardar(cliente);
        return toDTO(guardada);
    }

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
