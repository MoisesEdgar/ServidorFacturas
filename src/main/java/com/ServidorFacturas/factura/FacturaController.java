package com.ServidorFacturas.factura;

import com.ServidorFacturas.cliente.Cliente;
import com.ServidorFacturas.cliente.ClienteDTO;
import com.ServidorFacturas.partida.Partida;
import com.ServidorFacturas.partida.PartidaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository repoFactura;

    @Autowired
    private FacturaService serviceFactura;

    @GetMapping
    public List<FacturaDTO> getAll(){
        List<Factura> facturas = repoFactura.findAll();
        return facturas.stream().map(factura -> toDTO(factura)).collect(Collectors.toList());
    }

    @GetMapping("/anterior")
    public ResponseEntity<String> getUltimoFolio(){
        try{
            String folio = repoFactura.findUltimoFolio();
            return ResponseEntity.ok(folio);
        }catch(DataAccessException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/folio")
    public ResponseEntity<FacturaDTO> getByFolio(@RequestParam String folio){
        Factura factura = repoFactura.findByFolio(folio).orElse(null);
        if(factura == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(factura));
    }

    @GetMapping("/{id}")
    public FacturaDTO getById(@PathVariable Long id){
        Factura factura = repoFactura.findById(id).orElseThrow(() -> new RuntimeException("No se encontro la factura con el id " + id));
        return toDTO(factura);
    }

    @PostMapping
    public FacturaDTO save(@RequestBody FacturaDTO facturaDTO){
        Factura factura = toEntity(facturaDTO);
        Factura guardada = serviceFactura.guardar(factura);
        return toDTO(guardada);
    }

    @PutMapping("/{id}")
    public FacturaDTO update(@RequestBody FacturaDTO facturaDTO, @PathVariable Long id){
        Factura factura = toEntity(facturaDTO);
        Factura modificada = serviceFactura.updateFactura(factura ,id);

        return toDTO(modificada);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id){
        repoFactura.deleteById(id);
        return "Se elinimo la factura con id " + id;
    }


    private FacturaDTO toDTO(Factura factura){
       FacturaDTO dto = new FacturaDTO();
        dto.id = factura.getId();
        dto.folio = factura.getFolio();
        dto.fecha_expedicion = factura.getFechaExpedicion();
        dto.subtotal = factura.getSubtotal();
        dto.total = factura.getTotal();

        ClienteDTO cDTO = new ClienteDTO();

        cDTO.id = factura.getCliente().getId();
        cDTO.nombre = factura.getCliente().getNombre();
        cDTO.codigo = factura.getCliente().getCodigo();
        cDTO.telefono = factura.getCliente().getTelefono();
        cDTO.direccion = factura.getCliente().getDireccion();
        dto.cliente = cDTO;

        if (factura.getPartidas() != null){
            dto.partidas = new ArrayList<>();

            for(Partida partida : factura.getPartidas()){
                PartidaDTO pDTO = new PartidaDTO();
                pDTO.id = partida.getId();
                pDTO.nombre_articulo = partida.getNombreArticulo();
                pDTO.cantidad = partida.getCantidad();
                pDTO.precio = partida.getPrecio();
                pDTO.factura_id = factura.getId();
                dto.partidas.add(pDTO);
            }
        }
        return dto;
    }

    private Factura toEntity(FacturaDTO dto){
        Factura factura = new Factura();
        factura.setId(dto.id);
        factura.setFolio(dto.folio);
        factura.setFechaExpedicion(dto.fecha_expedicion);
        factura.setSubtotal(dto.subtotal);
        factura.setTotal(dto.total);

        Cliente cliente = new Cliente();

        cliente.setId(dto.cliente.id);
        cliente.setCodigo(dto.cliente.codigo);
        cliente.setNombre(dto.cliente.nombre);
        cliente.setTelefono(dto.cliente.telefono);
        cliente.setDireccion(dto.cliente.direccion);

        factura.setCliente(cliente);

        if(dto.partidas != null){
            factura.setPartidas(new ArrayList<>());

            for (PartidaDTO pDTO : dto.partidas){
                Partida partida = new Partida();
                partida.setId(pDTO.id);
                partida.setNombreArticulo(pDTO.nombre_articulo);
                partida.setCantidad(pDTO.cantidad);
                partida.setPrecio(pDTO.precio);

                factura.getPartidas().add(partida);
            }
        }
        return factura;
    }
}
