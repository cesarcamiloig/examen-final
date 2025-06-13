package com.examen.controller;

import com.examen.dto.ConsultaRequest;
import com.examen.dto.FacturaDetalleResponse;
import com.examen.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/consultar")
public class ConsultaController {

    private final ConsultaService consultaService;

    @Autowired
    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping("/{tiendaId}")
    public ResponseEntity<?> consultarFactura(
            @PathVariable UUID tiendaId,
            @RequestBody ConsultaRequest request) {
        
        try {
            FacturaDetalleResponse response = consultaService.consultarFactura(tiendaId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\", \"data\": null}");
        }
    }
}