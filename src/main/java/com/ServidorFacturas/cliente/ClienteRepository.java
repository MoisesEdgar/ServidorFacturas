package com.ServidorFacturas.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByNombre(@Param("nombre") String nombre);
    Cliente findByCodigo(@Param("codigo")String codigo);
}

