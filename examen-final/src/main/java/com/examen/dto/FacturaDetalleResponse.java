package com.examen.dto;

import java.math.BigDecimal;
import java.util.List;

public class FacturaDetalleResponse {
    private BigDecimal total;
    private BigDecimal impuestos;
    private Cliente cliente;
    private List<ProductoDetalle> productos;
    private Cajero cajero;

    public static class Cliente {
        private String documento;
        private String nombre;
        private String tipoDocumento;

        public String getDocumento() { return documento; }
        public void setDocumento(String documento) { this.documento = documento; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTipoDocumento() { return tipoDocumento; }
        public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    }

    public static class ProductoDetalle {
        private String referencia;
        private String nombre;
        private Integer cantidad;
        private BigDecimal precio;
        private BigDecimal descuento;
        private BigDecimal subtotal;

        public String getReferencia() { return referencia; }
        public void setReferencia(String referencia) { this.referencia = referencia; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecio() { return precio; }
        public void setPrecio(BigDecimal precio) { this.precio = precio; }
        public BigDecimal getDescuento() { return descuento; }
        public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }

    public static class Cajero {
        private String documento;
        private String nombre;

        public String getDocumento() { return documento; }
        public void setDocumento(String documento) { this.documento = documento; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getImpuestos() { return impuestos; }
    public void setImpuestos(BigDecimal impuestos) { this.impuestos = impuestos; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<ProductoDetalle> getProductos() { return productos; }
    public void setProductos(List<ProductoDetalle> productos) { this.productos = productos; }
    public Cajero getCajero() { return cajero; }
    public void setCajero(Cajero cajero) { this.cajero = cajero; }
}