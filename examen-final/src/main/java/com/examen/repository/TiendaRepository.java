package com.examen.repository;

import com.examen.model.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TiendaRepository extends JpaRepository<Tienda, UUID> {
}