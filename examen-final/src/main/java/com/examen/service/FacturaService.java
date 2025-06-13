package com.examen.service;

import com.examen.dto.FacturaRequest;
import com.examen.dto.FacturaResponse;
import java.util.UUID;

public interface FacturaService {
    FacturaResponse procesarFactura(UUID tiendaId, FacturaRequest request);
}