package com.ServidorFacturas.factura;
import com.ServidorFacturas.partida.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repoFactura;


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




    //PUT
    public Factura updateFactura(Factura factura, Long id) {
        Factura depDB = repoFactura.findById(id).orElseThrow(()-> new RuntimeException("Factura no encontrada"));

        if (Objects.nonNull(
                factura.getFolio()) && !"".equalsIgnoreCase(factura.getFolio())){
            depDB.setFolio(factura.getFolio());
        }

        if (Objects.nonNull(
                factura.getFechaExpedicion())){
            depDB.setFechaExpedicion(
                    factura.getFechaExpedicion());
        }

        if (Objects.nonNull(
                factura.getSubtotal())){
            depDB.setSubtotal(
                    factura.getSubtotal());
        }

        if (Objects.nonNull(
                factura.getTotal())){
            depDB.setTotal(
                    factura.getTotal());
        }

        if (Objects.nonNull(
                factura.getClienteId())){
            depDB.setClienteId(
                    factura.getClienteId());
        }


            for (Partida partida : factura.getPartidas()) {

                if(Objects.nonNull(
                        partida.getNombreArticulo())){
                    partida.setNombreArticulo(
                            partida.getNombreArticulo());
                }

                if(Objects.nonNull(partida.getCantidad())){
                    if(partida.getCantidad() <= 0){
                        throw new RuntimeException("La cantidad debe ser mayor a 0");
                    }else{
                        partida.setCantidad(partida.getCantidad());
                    }

                }

                if (Objects.nonNull(partida.getPrecio())){
                    if(partida.getPrecio() <=0){
                        throw new RuntimeException("El precio debe ser mayor o igual a 0.1");
                    }else{
                        partida.setPrecio(partida.getPrecio());
                    }
                }

                partida.setFactura(depDB);
                depDB.getPartidas().add(partida);

            }
        return repoFactura.save(depDB);
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
}

