package com.examen.repository;

import com.examen.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    Optional<Vendedor> findByDocumento(String documento);
}