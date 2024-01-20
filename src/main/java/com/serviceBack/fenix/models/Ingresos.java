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
    private String nit;

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
    private Double cif;

    @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor que {value}")
    @DecimalMax(value = "10000000.0", inclusive = false, message = "El valor debe ser menor que {value}")
    private Double impuestos;

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String idTransaccion;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
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

    public Double getCif() {
        return cif;
    }

    public void setCif(Double cif) {
        this.cif = cif;
    }

    public Double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Double impuestos) {
        this.impuestos = impuestos;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

}
