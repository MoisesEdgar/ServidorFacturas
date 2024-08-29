package com.ServidorFacturas.cliente;

import com.ServidorFacturas.factura.Factura;
import com.ServidorFacturas.factura.FacturaRepository;
import com.ServidorFacturas.partida.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private FacturaRepository repoFactura;

    @Autowired
    private PartidaRepository repoPartida;

    @PostMapping
    public Factura save(@RequestBody Factura factura){
        return repoFactura.save(factura);
    }

    @GetMapping
    public List<Factura> getAll(){
        return repoFactura.findAll();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        repoFactura.deleteById(id);
        return "se elimino la factura con el id ";
    }

}
