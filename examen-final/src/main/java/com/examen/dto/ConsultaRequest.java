package com.examen.dto;

public class ConsultaRequest {
    private String token;
    private String cliente;
    private Long factura;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public Long getFactura() { return factura; }
    public void setFactura(Long factura) { this.factura = factura; }
}