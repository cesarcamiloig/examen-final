package com.examen.service;

import com.examen.dto.ConsultaRequest;
import com.examen.dto.FacturaDetalleResponse;
import com.examen.exception.BusinessException;
import com.examen.model.*;
import com.examen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ConsultaServiceImpl implements ConsultaService {

    @Autowired private CajeroRepository cajeroRepository;
    @Autowired private CompraRepository compraRepository;

    @Override
    public FacturaDetalleResponse consultarFactura(UUID tiendaId, ConsultaRequest request) {
        // Validar cajero
        Cajero cajero = cajeroRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BusinessException("Token inválido", HttpStatus.UNAUTHORIZED));
        
        // Validar que el cajero pertenece a la tienda
        if (!cajero.getTienda().getId().equals(tiendaId)) {
            throw new BusinessException("No autorizado para esta tienda", HttpStatus.FORBIDDEN);
        }
        
        // Obtener la compra
        Compra compra = compraRepository.findById(request.getFactura())
                .orElseThrow(() -> new BusinessException("Factura no encontrada", HttpStatus.NOT_FOUND));
        
        // Validar que el cliente coincide
        if (!compra.getCliente().getDocumento().equals(request.getCliente())) {
            throw new BusinessException("Cliente no coincide con la factura", HttpStatus.FORBIDDEN);
        }
        
        // Construir respuesta
        return construirRespuesta(compra);
    }
    
    private FacturaDetalleResponse construirRespuesta(Compra compra) {
        FacturaDetalleResponse response = new FacturaDetalleResponse();
        response.setTotal(compra.getTotal());
        response.setImpuestos(compra.getImpuestos());
        
        // Cliente
        FacturaDetalleResponse.Cliente clienteDto = new FacturaDetalleResponse.Cliente();
        clienteDto.setDocumento(compra.getCliente().getDocumento());
        clienteDto.setNombre(compra.getCliente().getNombre());
        clienteDto.setTipoDocumento(compra.getCliente().getTipoDocumento().getNombre());
        response.setCliente(clienteDto);
        
        // Productos
        response.setProductos(compra.getDetalles().stream().map(detalle -> {
            FacturaDetalleResponse.ProductoDetalle prod = new FacturaDetalleResponse.ProductoDetalle();
            prod.setReferencia(detalle.getProducto().getReferencia());
            prod.setNombre(detalle.getProducto().getNombre());
            prod.setCantidad(detalle.getCantidad());
            prod.setPrecio(detalle.getPrecio());
            prod.setDescuento(detalle.getDescuento());
            
            BigDecimal subtotal = detalle.getPrecio().subtract(detalle.getDescuento())
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            prod.setSubtotal(subtotal);
            
            return prod;
        }).toList());
        
        // Cajero
        FacturaDetalleResponse.Cajero cajeroDto = new FacturaDetalleResponse.Cajero();
        cajeroDto.setDocumento(compra.getCajero().getDocumento());
        cajeroDto.setNombre(compra.getCajero().getNombre());
        response.setCajero(cajeroDto);
        
        return response;
    }
}