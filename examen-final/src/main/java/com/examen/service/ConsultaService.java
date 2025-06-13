package com.examen.service;

import com.examen.dto.ConsultaRequest;
import com.examen.dto.FacturaDetalleResponse;
import java.util.UUID;

public interface ConsultaService {
    FacturaDetalleResponse consultarFactura(UUID tiendaId, ConsultaRequest request);
}