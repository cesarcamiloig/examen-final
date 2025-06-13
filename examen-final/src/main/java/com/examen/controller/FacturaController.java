package com.examen.controller;

import com.examen.dto.FacturaRequest;
import com.examen.dto.FacturaResponse;
import com.examen.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/crear")
public class FacturaController {

    private final FacturaService facturaService;

    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping("/{tiendaId}")
    public ResponseEntity<?> crearFactura(
            @PathVariable UUID tiendaId,
            @RequestBody FacturaRequest request) {
        
        try {
            FacturaResponse response = facturaService.procesarFactura(tiendaId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\", \"data\": null}");
        }
    }
}