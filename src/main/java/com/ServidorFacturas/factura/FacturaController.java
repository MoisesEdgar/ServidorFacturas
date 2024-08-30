package com.ServidorFacturas.factura;

import com.ServidorFacturas.partida.Partida;
import com.ServidorFacturas.partida.PartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/facturas")
public class FacturaController {
    @Autowired
    private FacturaRepository repoFactura;



    @GetMapping("/{id}")
    public Factura getById(@PathVariable Long id) {
        Factura entidad = repoFactura.findById(id).orElseThrow(() -> new RuntimeException("No encontre la factura con el id " + id));
        return entidad;
    }


    @PostMapping
    public Factura save (@RequestBody Factura factura){
        return repoFactura.save(factura);
    }

    @DeleteMapping("/{Id}")
    public String delete(@PathVariable Long id){
            repoFactura.deleteById(id);
            return "se elimino la factura con el id" + id;
    }




}
