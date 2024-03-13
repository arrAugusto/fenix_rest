package com.serviceBack.fenix.models;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.DecimalMax;

public class Ingresos {

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String usuario;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String idNit;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String canalDigital;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "El campo debe no contener caracteres especiales")
    private String fechaOperativa;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String documento;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String codigoQR;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int bultos;

    @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor que {value}")
    @DecimalMax(value = "99999999.0", inclusive = false, message = "El valor debe ser menor que {value}")
    private Double montoTotal;

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String idTransaccion;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String idImages;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "El campo debe no contener caracteres especiales")
    private String fechaGarita;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "El campo debe no contener caracteres especiales")
    private String fechaBodega;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String codigo_transaccion;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String area;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String documento_top_pay;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String document;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String nombre;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String boleta_de_pago;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String comments;

    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El campo debe no contener caracteres especiales")
    private String auth_transaction;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdNit() {
        return idNit;
    }

    public void setIdNit(String idNit) {
        this.idNit = idNit;
    }

    public String getCanalDigital() {
        return canalDigital;
    }

    public void setCanalDigital(String canalDigital) {
        this.canalDigital = canalDigital;
    }

    public String getFechaOperativa() {
        return fechaOperativa;
    }

    public void setFechaOperativa(String fechaOperativa) {
        this.fechaOperativa = fechaOperativa;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public int getBultos() {
        return bultos;
    }

    public void setBultos(int bultos) {
        this.bultos = bultos;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getIdImages() {
        return idImages;
    }

    public void setIdImages(String idImages) {
        this.idImages = idImages;
    }

    public String getFechaGarita() {
        return fechaGarita;
    }

    public void setFechaGarita(String fechaGarita) {
        this.fechaGarita = fechaGarita;
    }

    public String getFechaBodega() {
        return fechaBodega;
    }

    public void setFechaBodega(String fechaBodega) {
        this.fechaBodega = fechaBodega;
    }

    public String getCodigo_transaccion() {
        return codigo_transaccion;
    }

    public void setCodigo_transaccion(String codigo_transaccion) {
        this.codigo_transaccion = codigo_transaccion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDocumento_top_pay() {
        return documento_top_pay;
    }

    public void setDocumento_top_pay(String documento_top_pay) {
        this.documento_top_pay = documento_top_pay;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBoleta_de_pago() {
        return boleta_de_pago;
    }

    public void setBoleta_de_pago(String boleta_de_pago) {
        this.boleta_de_pago = boleta_de_pago;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAuth_transaction() {
        return auth_transaction;
    }

    public void setAuth_transaction(String auth_transaction) {
        this.auth_transaction = auth_transaction;
    }

}
