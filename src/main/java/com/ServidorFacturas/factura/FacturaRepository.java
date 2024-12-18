package com.ServidorFacturas.factura;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long>, FacturaRepositoryCustom {
    Optional<Factura> findByFolio(String folio);

//    @Query("SELECT f.folio FROM Factura f ORDER BY f.id DESC LIMIT 1")
//    Optional<String> findUltimoFolio();
}
