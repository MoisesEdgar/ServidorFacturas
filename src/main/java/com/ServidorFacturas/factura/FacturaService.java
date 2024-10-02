package com.ServidorFacturas.factura;
import com.ServidorFacturas.partida.Partida;
import com.ServidorFacturas.partida.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repoFactura;
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
        String folioFinal = "";
        if (folio.matches("^F-\\d\\d\\d")) {
            List<Factura> facturas = repoFactura.findAll();

            for (Factura factura : facturas){
               folioFinal = factura.getFolio();
            }

            if(folioFinal.isEmpty()){
                if(folio.equalsIgnoreCase("F-001")){
                }else{
                    throw new RuntimeException("El formato del folio no es valido. La nuemracion debe ser: F-001");
                }
            }else{
                Integer intFolio = getNumeracion(folio);
                Integer intFolioFinal = getNumeracion(folioFinal);

                if(intFolio != (intFolioFinal + 1)){
                    String ceros = "";

                        for (int i = String.valueOf(intFolioFinal).length(); i < 3;) {
                            i++;
                            ceros = ceros + "0";
                        }

                    throw new RuntimeException("El formato del folio no es valido. La nuemracion debe ser F-" + ceros + (intFolioFinal + 1));
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

    public void calcularTotales(Long id){
        Factura factura = repoFactura.findById(id).orElseThrow(()-> new RuntimeException("Factura no encontrada"));
        Double subtotal = 0.0;
        Double total = 0.0;
        Integer cantidad = 0;
        Double precio = 0.0;

        if (Objects.nonNull(factura.getPartidas()) && !factura.getPartidas().isEmpty()) {
            for (Partida partida : factura.getPartidas()) {

                cantidad = partida.getCantidad();
                precio = partida.getPrecio();

                subtotal += cantidad * precio;
            }
            total = subtotal + subtotal * 0.16;

            factura.setSubtotal(Math.round(subtotal * 100)/100d);
            factura.setTotal(Math.round(total * 100)/100d);

        }
        repoFactura.save(factura);
    }







    public Factura updateFactura(Factura factura, Long id) {
        Factura depDB = repoFactura.findById(id).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        List<Partida> partidasActuales = depDB.getPartidas();
        List<Partida> partidasActualizadas = new ArrayList<>();

        for (Partida partida : factura.getPartidas()) {
            if (partida.getId() != null) {

                Partida partidaExistente = partidasActuales.stream()
                        .filter(partidaActual -> partidaActual.getId().equals(partida.getId()))
                        .findFirst()
                        .orElse(null);


                if (partidaExistente != null) {

                    if (Objects.nonNull(partida.getNombreArticulo())) {
                        partidaExistente.setNombreArticulo(partida.getNombreArticulo());
                    } else {
                        throw new RuntimeException("No se especifico un nombre");
                    }

                    if (Objects.nonNull(partida.getCantidad()) && partida.getCantidad() > 0) {
                        partidaExistente.setCantidad(partida.getCantidad());
                    } else {
                        throw new RuntimeException("La cantidad debe ser mayor a 0");
                    }

                    if (Objects.nonNull(partida.getPrecio()) && partida.getPrecio() >= 0.1) {
                        partidaExistente.setPrecio(partida.getPrecio());
                    } else {
                        throw new RuntimeException("El precio debe ser mayor o igual a 0.1");
                    }

                    partidasActualizadas.add(partidaExistente);
                }
            } else {
                partida.setFactura(depDB);
                partidasActualizadas.add(partida);

            }
        }


//        for (Partida partida : factura.getPartidas()) {
//            Partida partidaExistente = partidasActuales.stream()
//                    .filter(partidaActual-> partidaActual.getId().equals(partida.getId()))
//                    .findFirst()
//                    .orElse(null);
//
//            if (partidaExistente == null) {
//                repoPartida.deleteById(partida.getId());
//            }
//        }

        depDB.setPartidas(partidasActualizadas);
        return repoFactura.save(depDB);
    }









}




















