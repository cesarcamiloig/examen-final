package com.examen.service;

import com.examen.dto.FacturaRequest;
import com.examen.dto.FacturaResponse;
import com.examen.exception.BusinessException;
import com.examen.model.*;
import com.examen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired private TiendaRepository tiendaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private TipoDocumentoRepository tipoDocumentoRepository;
    @Autowired private VendedorRepository vendedorRepository;
    @Autowired private CajeroRepository cajeroRepository;
    @Autowired private TipoPagoRepository tipoPagoRepository;
    @Autowired private CompraRepository compraRepository;

    @Transactional
    @Override
    public FacturaResponse procesarFactura(UUID tiendaId, FacturaRequest request) {
        // 1. Validar tienda
        Tienda tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new BusinessException("Tienda no encontrada", HttpStatus.NOT_FOUND));

        // 2. Validar campos obligatorios
        validarCamposObligatorios(request);

        // 3. Procesar cliente
        Cliente cliente = procesarCliente(request.getCliente());

        // 4. Validar y procesar productos
        List<DetalleCompra> detalles = procesarProductos(request.getProductos());

        // 5. Calcular totales
        BigDecimal total = calcularTotal(detalles);
        BigDecimal impuestos = request.getImpuesto() != null ? request.getImpuesto() : BigDecimal.ZERO;
        BigDecimal totalConImpuestos = total.add(impuestos);

        // 6. Validar pagos
        validarPagos(request.getMediosPago(), totalConImpuestos, tienda);

        // 7. Validar vendedor y cajero
        Vendedor vendedor = validarVendedor(request.getVendedor());
        Cajero cajero = validarCajero(request.getCajero(), tienda);

        // 8. Crear y guardar compra
        Compra compra = new Compra();
        compra.setTienda(tienda);
        compra.setCliente(cliente);
        compra.setVendedor(vendedor);
        compra.setCajero(cajero);
        compra.setTotal(totalConImpuestos);
        compra.setImpuestos(impuestos);
        compra.setFecha(LocalDate.now());

        // Asociar detalles
        detalles.forEach(detalle -> detalle.setCompra(compra));
        compra.setDetalles(detalles);
        
        // Guardar compra
        Compra savedCompra = compraRepository.save(compra);

        // 9. Actualizar inventario
        actualizarInventario(detalles);

        // 10. Retornar respuesta
        return new FacturaResponse(
            "success",
            "La factura se ha creado correctamente con el número: " + savedCompra.getId(),
            new FacturaResponse.Data(
                savedCompra.getId().toString(),
                savedCompra.getTotal().toString(),
                savedCompra.getFecha().toString()
            )
        );
    }

    private void validarCamposObligatorios(FacturaRequest request) {
        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            throw new BusinessException("No hay productos asignados para esta compra", HttpStatus.NOT_FOUND);
        }
        if (request.getMediosPago() == null || request.getMediosPago().isEmpty()) {
            throw new BusinessException("No hay medios de pagos asignados para esta compra", HttpStatus.NOT_FOUND);
        }
        if (request.getCliente() == null) {
            throw new BusinessException("No hay información del cliente", HttpStatus.NOT_FOUND);
        }
        if (request.getVendedor() == null) {
            throw new BusinessException("No hay información del vendedor", HttpStatus.NOT_FOUND);
        }
        if (request.getCajero() == null) {
            throw new BusinessException("No hay información del cajero", HttpStatus.NOT_FOUND);
        }
    }

    private Cliente procesarCliente(FacturaRequest.Cliente clienteRequest) {
        // Buscar cliente por documento
        Optional<Cliente> clienteOpt = clienteRepository.findByDocumento(clienteRequest.getDocumento());
        
        if (clienteOpt.isPresent()) {
            return clienteOpt.get();
        }
        
        // Crear nuevo cliente si no existe
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByNombre(clienteRequest.getTipoDocumento())
                .orElseThrow(() -> new BusinessException("Tipo de documento no válido", HttpStatus.BAD_REQUEST));
        
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(clienteRequest.getNombre());
        nuevoCliente.setDocumento(clienteRequest.getDocumento());
        nuevoCliente.setTipoDocumento(tipoDocumento);
        
        return clienteRepository.save(nuevoCliente);
    }

    private List<DetalleCompra> procesarProductos(List<FacturaRequest.Producto> productos) {
        List<DetalleCompra> detalles = new ArrayList<>();
        
        for (FacturaRequest.Producto productoReq : productos) {
            Producto producto = productoRepository.findByReferencia(productoReq.getReferencia())
                    .orElseThrow(() -> new BusinessException(
                        "La referencia del producto " + productoReq.getReferencia() + " no existe", 
                        HttpStatus.NOT_FOUND));
            
            if (producto.getCantidad() < productoReq.getCantidad()) {
                throw new BusinessException(
                    "La cantidad a comprar supera el máximo del producto en tienda", 
                    HttpStatus.FORBIDDEN);
            }
            
            DetalleCompra detalle = new DetalleCompra();
            detalle.setProducto(producto);
            detalle.setCantidad(productoReq.getCantidad());
            detalle.setPrecio(producto.getPrecio());
            detalle.setDescuento(productoReq.getDescuento() != null ? 
                productoReq.getDescuento() : BigDecimal.ZERO);
            
            detalles.add(detalle);
        }
        return detalles;
    }

    private BigDecimal calcularTotal(List<DetalleCompra> detalles) {
        BigDecimal total = BigDecimal.ZERO;
        
        for (DetalleCompra detalle : detalles) {
            BigDecimal precioNeto = detalle.getPrecio().subtract(detalle.getDescuento());
            BigDecimal subtotal = precioNeto.multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
        }
        
        return total;
    }

    private void validarPagos(List<FacturaRequest.MedioPago> mediosPago, BigDecimal total, Tienda tienda) {
        BigDecimal sumaPagos = BigDecimal.ZERO;
        
        for (FacturaRequest.MedioPago medio : mediosPago) {
            // Validar tipo de pago
            if (!tipoPagoRepository.existsByNombre(medio.getTipoPago())) {
                throw new BusinessException("Tipo de pago no permitido en la tienda", HttpStatus.FORBIDDEN);
            }
            
            sumaPagos = sumaPagos.add(medio.getValor());
        }
        
        if (sumaPagos.compareTo(total) != 0) {
            throw new BusinessException("El valor de la factura no coincide con el valor total de los pagos", HttpStatus.FORBIDDEN);
        }
    }

    private Vendedor validarVendedor(FacturaRequest.Vendedor vendedorRequest) {
        return vendedorRepository.findByDocumento(vendedorRequest.getDocumento())
                .orElseThrow(() -> new BusinessException("El vendedor no existe en la tienda", HttpStatus.NOT_FOUND));
    }

    private Cajero validarCajero(FacturaRequest.Cajero cajeroRequest, Tienda tienda) {
        Cajero cajero = cajeroRepository.findByToken(cajeroRequest.getToken())
                .orElseThrow(() -> new BusinessException("El token no corresponde a ningún cajero en la tienda", HttpStatus.NOT_FOUND));
        
        if (!cajero.getTienda().getId().equals(tienda.getId())) {
            throw new BusinessException("El cajero no está asignado a esta tienda", HttpStatus.FORBIDDEN);
        }
        
        return cajero;
    }

    private void actualizarInventario(List<DetalleCompra> detalles) {
        for (DetalleCompra detalle : detalles) {
            Producto producto = detalle.getProducto();
            int nuevaCantidad = producto.getCantidad() - detalle.getCantidad();
            producto.setCantidad(nuevaCantidad);
            productoRepository.save(producto);
        }
    }
}