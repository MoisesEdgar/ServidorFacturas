package com.ServidorFacturas.factura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface FacturaRepository extends JpaRepository<Factura, Long>, FacturaRepositoryCustom {
    Optional<Factura> findByFolio(String folio);
}
