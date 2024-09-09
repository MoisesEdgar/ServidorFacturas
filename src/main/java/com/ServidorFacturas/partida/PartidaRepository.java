package com.ServidorFacturas.partida;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.JdbcRowSet;

@Repository
public interface PartidaRepository extends JpaRepository< Partida, Long> {
}
