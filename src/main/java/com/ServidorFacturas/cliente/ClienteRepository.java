package com.ServidorFacturas.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNombre(String nombre);
    Optional<Cliente> findByCodigo(String codigo);

    @Query("SELECT c.codigo FROM Cliente c ORDER BY c.id DESC LIMIT 1")
    Optional<String> findUltimoCodigo();

}

