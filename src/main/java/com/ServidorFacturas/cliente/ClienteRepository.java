package com.ServidorFacturas.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNombre(@Param("nombre") String nombre);
    Optional<Cliente> findByCodigo(@Param("codigo")String codigo);
}

