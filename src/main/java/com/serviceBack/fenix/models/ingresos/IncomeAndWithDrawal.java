package com.serviceBack.fenix.models.ingresos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IncomeAndWithDrawal {

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int cliente_id;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int usuario_id;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int id_bodega;

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe contener solo dígitos del 0 al 9")
    private String id_transaccion;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String referencia_almacen;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String canal_digital;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String auth_transaction;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String numero_factura;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String estado;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String fecha;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String fecha_operativa;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int bultos;
    private double valor;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String bl;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String documento;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String codigoQR;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String acta;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String arribo;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_1;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_2;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_3;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_4;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_5;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_6;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_7;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_8;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String codigo_transaccion;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String idImages;

    @Pattern(regexp = "^[a-zA-Z0-9_\\-\\s]+$", message = "El campo debe no contener caracteres especiales")
    private String area;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String documento_topay;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String nombre;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "El campo debe no contener caracteres especiales")
    private String boleta_de_pago;

    @Pattern(regexp = "^[a-zA-Z0-9_\\-\\s]+$", message = "El campo debe no contener caracteres especiales")
    private String comments;

}
