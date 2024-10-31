package com.ServidorFacturas.factura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByFolio(String folio);

    @Query("SELECT f FROM Factura f ORDER BY f.id DESC LIMIT 1")
    Optional<Factura> findByOrderBYidDesc();
}
