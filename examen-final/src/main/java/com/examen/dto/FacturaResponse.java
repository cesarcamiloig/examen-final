package com.examen.dto;

public class FacturaResponse {
    private String status;
    private String message;
    private Data data;

    public FacturaResponse(String status, String message, Data data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static class Data {
        private String numero;
        private String total;
        private String fecha;

        public Data(String numero, String total, String fecha) {
            this.numero = numero;
            this.total = total;
            this.fecha = fecha;
        }

        public String getNumero() { return numero; }
        public String getTotal() { return total; }
        public String getFecha() { return fecha; }
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Data getData() { return data; }
}