package com.serviceBack.fenix.models.ingresos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.DecimalMax;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Ingresos {

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String idTransaccion;

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String usuario;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String idNit;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String idImages;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String fechaGarita;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String fechaBodega;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String fechaOperativa;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String codigo_transaccion;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String documento;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String codigoQR;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int bultos;

    @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor que {value}")
    @DecimalMax(value = "99999999.0", inclusive = false, message = "El valor debe ser menor que {value}")
    private Double montoTotal;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String area;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String documento_top_pay;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String document;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String nombre;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String boleta_de_pago;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String comments;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String canalDigital;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El campo debe no contener caracteres especiales")
    private String auth_transaction;
}
