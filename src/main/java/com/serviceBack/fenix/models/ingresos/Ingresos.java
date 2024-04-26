package com.serviceBack.fenix.models.ingresos;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Ingresos {
    
    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int p_cliente_id;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int p_usuario_id;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int p_id_bodega;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int p_id_transaccion;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_referencia_almacen;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_canal_digital;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_auth_transaction;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_numero_factura;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_estado;
    
    
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String p_fecha;
    
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String p_fecha_operativa;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int p_bultos;
    private double p_valor;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_bl;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_documento;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_codigoQR;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_acta;
    
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String p_arribo;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_generica_1;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_generica_2;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_generica_3;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_generica_4;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_generica_5;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_generica_6;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_generica_7;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_generica_8;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_codigo_respuesta;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_mensaje_respuesta;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_codigo_transaccion;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_idImages;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_area;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_documento_top_pay;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_p_documento;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_nombre;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_boleta_de_pago;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El campo debe no contener caracteres especiales")
    private String p_comments;

}
