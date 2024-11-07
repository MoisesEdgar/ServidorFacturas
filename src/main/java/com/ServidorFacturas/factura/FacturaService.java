package com.ServidorFacturas.factura;

import com.ServidorFacturas.cliente.ClienteRepository;
import com.ServidorFacturas.partida.PartidaRepository;
import com.ServidorFacturas.partida.Partida;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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

    public Factura guardar(Factura factura){

        if(factura.getFolio() == null || factura.getFolio().isEmpty()){
            throw new RuntimeException("No se especifico el folio de la factura");
        }

        factura.setFechaExpedicion(new Date());

        for (Partida partida : factura.getPartidas()) {

            if(partida.getNombreArticulo() == null || partida.getNombreArticulo().isEmpty()){
                throw new RuntimeException("No se especifico el nombre del articulo");
            }

            Integer cantidad = partida.getCantidad();
            if(cantidad <= 0){
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }

            Double precio = partida.getPrecio();
            if(precio < 0.1){
                throw new RuntimeException("El precio debe ser mayor a 0.1");
            }

            partida.setFactura(factura);
        }

        validarFormatoFolio(factura.getFolio());

        Map<String, Double> totales = calcularTotales(factura);

        factura.setSubtotal(totales.get("subtotal"));
        factura.setTotal(totales.get("total"));

        return repoFactura.save(factura);
    }

    public Factura updateFactura(Factura factura, Long id) {
        Factura facturaActual = repoFactura.findById(id).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        List<Partida> partidasActuales = new ArrayList<>(facturaActual.getPartidas());
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
                partida.setFactura(facturaActual);
                Partida nuevaPartida = repoPartida.save(partida);
                facturaActual.getPartidas().add(nuevaPartida);
            }
        }


        List<Long> idsEliminar = partidasActuales.stream()
                .filter(partidaActual -> !idsPartidas.contains(partidaActual.getId()))
                .map(Partida::getId)
                .collect(Collectors.toList());


        facturaActual.getPartidas().removeIf(partidaActual -> idsEliminar.contains(partidaActual.getId()));

        Map<String, Double> totales = calcularTotales(facturaActual);

        facturaActual.setSubtotal(totales.get("subtotal"));
        facturaActual.setTotal(totales.get("total"));
        return repoFactura.save(facturaActual);
    }

    private void validarFormatoFolio(String folio){
        String folioAnterior = "";

        try{
           folioAnterior = repoFactura.findUltimoFolio();
        }catch(DataAccessException ex){
            folioAnterior = null;
        }

        if (folio.matches("^F-\\d\\d\\d")) {
            if(folioAnterior == null){
                if(!folio.equalsIgnoreCase("F-001")){
                    throw new RuntimeException("El formato del folio no es valido. La nuemracion debe ser: F-001");
                }
            }else{
                Integer nuevaNumeracion = getNumeracionFolio(folio);
                Integer anteriorNumeracion = getNumeracionFolio(folioAnterior);

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

    private Integer getNumeracionFolio(String folio){
        String[] salto = folio.split("-");
        return Integer.parseInt(salto[1]);
    }

    private Map<String, Double> calcularTotales(Factura factura) {
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
        }else{
            throw new RuntimeException("Error en los valores de la partida.");
        }

        Map<String, Double> totales = new HashMap<>();
        totales.put("subtotal", Math.round(subtotal * 100) /100d);
        totales.put("total", Math.round(total * 100) /100d);

        return totales;
    }

}