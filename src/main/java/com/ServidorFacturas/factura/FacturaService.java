package com.ServidorFacturas.factura;
import com.ServidorFacturas.cliente.ClienteRepository;
import com.ServidorFacturas.partida.Partida;
import com.ServidorFacturas.partida.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repoFactura;

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private PartidaRepository repoPartida;

    //AGREGA
    public Factura guardar(Factura factura){
        Double subtotal = 0.0;
        Double total = 0.0;

        //FOLIO
        if(factura.getFolio() == null || factura.getFolio().isEmpty()){
            throw new RuntimeException("No se especifico el folio de la factura");
        }

        //FECHA
        factura.setFechaExpedicion(new Date());

        //ID_CLIENTE
        if(factura.getClienteId() == null){
            throw new RuntimeException("No se especifico el id del cliente");
        }else{
           Long id = factura.getClienteId();
           repoCliente.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el Cliente con el id " + id));
        }

        //PARTIDAS
        for (Partida partida : factura.getPartidas()) {

            //NOMBRE_ARTICULO
            if(partida.getNombreArticulo() == null || partida.getNombreArticulo().isEmpty()){
                throw new RuntimeException("No se especifico el nombre del articulo");
            }

            //CANTIDAD
            Integer cantidad = partida.getCantidad();

            if(cantidad <= 0){
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }

            //PRECIO
            Double precio = partida.getPrecio();
            if(precio < 0.1){
                throw new RuntimeException("El precio debe ser mayor a 0.1");
            }

            subtotal += cantidad * precio;
            //ID_FACTURA
            partida.setFactura(factura);
        }

        total = subtotal + subtotal * 0.16;

        //SUBTOTAL
        factura.setSubtotal(Math.round(subtotal * 100)/100d);

        //TOTAL
        factura.setTotal(Math.round(total * 100)/100d);

        validarFolio(factura.getFolio());



        return repoFactura.save(factura);
    }

    public void validarFolio(String folio){
        if (folio.matches("^F-\\d\\d\\d")) {

            String folioAnterior = repoFactura.findByOrderBYidDesc().orElse(null);

            if(folioAnterior == null){
                if(!folio.equalsIgnoreCase("F-001")){
                    throw new RuntimeException("El formato del folio no es valido. La nuemracion debe ser: F-001");
                }
            }else{
                Integer nuevaNumeracion = getNumeracion(folio);
                Integer anteriorNumeracion = getNumeracion(folioAnterior);

                if(nuevaNumeracion  != (anteriorNumeracion  + 1)){
                    String ceros = "";

                        for (int i = String.valueOf(anteriorNumeracion ).length(); i < 3;) {
                            i++;
                            ceros = ceros + "0";
                        }

                    throw new RuntimeException("El formato del folio no es valido. La nuemracion debe ser F-" + ceros + (anteriorNumeracion  + 1));
                }
            }
        }else{
            throw new RuntimeException("El formato del folio no es valido debe ser: F-000");
        }
    }

    public Integer getNumeracion(String folio){
        String[] salto = folio.split("-");
        return Integer.parseInt(salto[1]);
    }

    public void calcularTotales(Long id) {
        Factura factura = repoFactura.findById(id).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        double subtotal = 0.0;
        double total = 0.0;

        if (Objects.nonNull(factura.getPartidas()) && !factura.getPartidas().isEmpty()) {
            for (Partida partida : factura.getPartidas()) {
                int cantidad = partida.getCantidad();
                double precio = partida.getPrecio();

                if (cantidad > 0 && precio >= 0.1) {
                    subtotal += cantidad * precio;
                } else {
                    throw new RuntimeException("Error en los valores de la partida. Cantidad o precio inválido.");
                }
            }
            total = subtotal + (subtotal * 0.16);

            factura.setSubtotal(Math.round(subtotal * 100) / 100.0);
            factura.setTotal(Math.round(total * 100) / 100.0);
        }
        repoFactura.save(factura);
    }





    public Factura updateFactura(Factura factura, Long id) {
        Factura depDB = repoFactura.findById(id).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        List<Partida> partidasActuales = new ArrayList<>(depDB.getPartidas());
        List<Long> idsPartidas = new ArrayList<>();


        for (Partida partida : factura.getPartidas()) {

            if (Objects.isNull(partida.getNombreArticulo())) {
                throw new RuntimeException("No se especificó un nombre");
            }
            if (Objects.isNull(partida.getCantidad()) || partida.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }
            if (Objects.isNull(partida.getPrecio()) || partida.getPrecio() < 0.1) {
                throw new RuntimeException("El precio debe ser mayor o igual a 0.1");
            }

            if (partida.getId() != null) {
                Partida partidaExistente = partidasActuales.stream()
                        .filter(p -> p.getId().equals(partida.getId()))
                        .findFirst().orElse(null);

                if (partidaExistente != null) {
                    partidaExistente.setNombreArticulo(partida.getNombreArticulo());
                    partidaExistente.setCantidad(partida.getCantidad());
                    partidaExistente.setPrecio(partida.getPrecio());
                    idsPartidas.add(partida.getId());
                }
            } else {
                partida.setFactura(depDB);
                Partida nuevaPartida = repoPartida.save(partida);
                depDB.getPartidas().add(nuevaPartida);
            }
        }


        List<Long> idsEliminar = partidasActuales.stream()
                .filter(partidaActual -> !idsPartidas.contains(partidaActual.getId()))
                .map(Partida::getId)
                .collect(Collectors.toList());


        depDB.getPartidas().removeIf(partidaActual -> idsEliminar.contains(partidaActual.getId()));


        return repoFactura.save(depDB);
    }

}