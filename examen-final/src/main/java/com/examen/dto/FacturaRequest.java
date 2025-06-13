package com.examen.dto;

import java.math.BigDecimal;
import java.util.List;

public class FacturaRequest {
    private BigDecimal impuesto;
    private Cliente cliente;
    private List<Producto> productos;
    private List<MedioPago> mediosPago;
    private Vendedor vendedor;
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

    public static class Producto {
        private String referencia;
        private Integer cantidad;
        private BigDecimal descuento;

        public String getReferencia() { return referencia; }
        public void setReferencia(String referencia) { this.referencia = referencia; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getDescuento() { return descuento; }
        public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    }

    public static class MedioPago {
        private String tipoPago;
        private String tipoTarjeta;
        private Integer cuotas;
        private BigDecimal valor;

        public String getTipoPago() { return tipoPago; }
        public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }
        public String getTipoTarjeta() { return tipoTarjeta; }
        public void setTipoTarjeta(String tipoTarjeta) { this.tipoTarjeta = tipoTarjeta; }
        public Integer getCuotas() { return cuotas; }
        public void setCuotas(Integer cuotas) { this.cuotas = cuotas; }
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
    }

    public static class Vendedor {
        private String documento;

        public String getDocumento() { return documento; }
        public void setDocumento(String documento) { this.documento = documento; }
    }

    public static class Cajero {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    public BigDecimal getImpuesto() { return impuesto; }
    public void setImpuesto(BigDecimal impuesto) { this.impuesto = impuesto; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
    public List<MedioPago> getMediosPago() { return mediosPago; }
    public void setMediosPago(List<MedioPago> mediosPago) { this.mediosPago = mediosPago; }
    public Vendedor getVendedor() { return vendedor; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }
    public Cajero getCajero() { return cajero; }
    public void setCajero(Cajero cajero) { this.cajero = cajero; }
}