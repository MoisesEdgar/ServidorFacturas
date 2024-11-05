package com.ServidorFacturas.factura;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;


@Repository
public class FacturaRepositoryImpl implements FacturaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String findUltimoFolio(){
        TypedQuery<String> query = entityManager.createQuery("SELECT f.folio FROM Factura f ORDER BY f.id DESC LIMIT 1", String.class);
        return query.getSingleResult();
    }
}
