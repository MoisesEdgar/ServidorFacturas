package com.ServidorFacturas.factura;

import com.ServidorFacturas.partida.Partida;
import com.ServidorFacturas.partida.PartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/ultima")
    public ResponseEntity<FacturaDTO> getUltimo(){
        Factura factura = repoFactura.findByOrderBYidDesc().orElse(null);
        if(factura == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(factura));
    }

    @GetMapping("/folio")
    public ResponseEntity<FacturaDTO> getByFolio(@RequestParam(required = false) String folio){
        Factura factura = repoFactura.findByFolio(folio).orElse(null);
        if(factura == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(factura));
    }



    @GetMapping("/{id}")
    public FacturaDTO getByID(@PathVariable Long id){
        Factura entidad = repoFactura.findById(id).orElseThrow(() -> new RuntimeException("No se encontro la factura con el id " + id));
        return toDTO(entidad);
    }

    @PostMapping
    public FacturaDTO save(@RequestBody FacturaDTO facturaDTO){
        Factura factura = toEntity(facturaDTO);
        Factura guardada = serviceFactura.guardar(factura);
        return toDTO(guardada);
    }

    @PutMapping("/{id}")
    public FacturaDTO updateFactura(@RequestBody FacturaDTO facturaDTO, @PathVariable Long id){
        Factura factura = toEntity(facturaDTO);
        Factura modificada = serviceFactura.updateFactura(factura ,id);
        serviceFactura.calcularTotales(id);
        return toDTO(modificada);
    }

    @DeleteMapping("/{id}")
    public String deleteFacturaById(@PathVariable Long id){
        repoFactura.findById(id).orElseThrow(() -> new RuntimeException("No se encontro la factura con el id " + id));
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
        dto.cliente_id = factura.getClienteId();
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
        factura.setClienteId(dto.cliente_id);

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
