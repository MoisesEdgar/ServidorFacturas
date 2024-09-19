package com.ServidorFacturas.partida;

import com.ServidorFacturas.cliente.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partidas")
public class PartidaController {
    @Autowired
    private PartidaRepository repoPartida;

    @Autowired
    private PartidaService servicePartida;

/*
    @PostMapping
    private PartidaDTO save(@RequestBody PartidaDTO partidaDTO){
        Partida partida = toEntity(partidaDTO);
        Partida guardada = servicePartida.guardar(partida);

        return toDTO(guardada);
    }
    */
    @DeleteMapping("/{id}")
    private String deletepartidaById(@PathVariable Long id){
        repoPartida.findById(id).orElseThrow(() -> new RuntimeException("No se encontro la partida con el id " + id));
        repoPartida.deleteById(id);
        return "Se elinimo la partida con id " + id;
    }



/*
    private PartidaDTO toDTO(Partida partida){
        PartidaDTO dto = new PartidaDTO();
        dto.id = partida.getId();
        dto.nombre_articulo = partida.getNombreArticulo();
        dto.cantidad = partida.getCantidad();
        dto.precio = partida.getPrecio();
        dto.factura_id = partida.getFactura().getId();

        return dto;
    }

    private Partida toEntity(PartidaDTO dto){
        Partida partida = new Partida();
        partida.setId(dto.id);
        partida.setNombreArticulo(dto.nombre_articulo);
        partida.setCantidad(dto.cantidad);
        partida.setPrecio(dto.precio);


        return partida;
    }
*/
}
