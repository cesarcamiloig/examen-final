package com.examen.repository;

import com.examen.model.TipoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoPagoRepository extends JpaRepository<TipoPago, Long> {
    Optional<TipoPago> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}