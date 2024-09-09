package com.ServidorFacturas.partida;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partidas")
public class PartidaController {
    @Autowired
    private PartidaRepository repoPartida;

    @DeleteMapping("/{id}")
    private String deletepartidaById(@PathVariable Long id){
        repoPartida.findById(id).orElseThrow(() -> new RuntimeException("No se encontro la factura con el id " + id));
        repoPartida.deleteById(id);
        return "Se elinimo la factura con id " + id;
    }

}
