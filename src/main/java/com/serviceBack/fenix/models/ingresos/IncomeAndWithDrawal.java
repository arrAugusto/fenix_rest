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

    @Pattern(regexp = "^[0-9]*$", message = "El campo debe contener solo dígitos del 0 al 9")
    private String id_transaccion;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String referencia_almacen;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String canal_digital;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String auth_transaction;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String numero_factura;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String estado;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String fecha;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String fecha_operativa;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int bultos;

    private double valor;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String bl;

    @Pattern(regexp = "^[a-zA-Z0-9:]*$", message = "El campo debe no contener caracteres especiales")
    private String documento;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String codigoQR;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String acta;

    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String arribo;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String generica_1;

    @Pattern(regexp = "[a-zA-Z0-9áéíóúüÁÉÍÓÚÜñÑ\\s.-]*$", message = "El campo debe no contener caracteres especiales")
    private String generica_2;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String generica_3;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String generica_4;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String generica_5;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String generica_6;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String generica_7;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String generica_8;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String codigo_transaccion;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String idImages;

    @Pattern(regexp = "^[a-zA-Z0-9_\\-\\s]*$", message = "El campo debe no contener caracteres especiales")
    private String area;

    @Pattern(regexp = "^[a-zA-Z0-9._-]*$", message = "El campo debe no contener caracteres especiales")
    private String documento_topay;

    @Pattern(regexp = "[a-zA-Z0-9áéíóúüÁÉÍÓÚÜñÑ\\s.-]*$", message = "El campo debe no contener caracteres especiales")
    private String nombre;

    @Pattern(regexp = "[a-zA-Z0-9áéíóúüÁÉÍÓÚÜñÑ\\s.-]*$", message = "El campo debe no contener caracteres especiales")
    private String nombre_cliente;

    @Pattern(regexp = "[\\wáéíóúüÁÉÍÓÚÜñÑ\\s\\.-]*", message = "El campo solo debe contener letras, números, espacios, puntos o guiones.")
    private String boleta_de_pago;

    @Pattern(regexp = "^[a-zA-Z0-9_\\-\\s]*$", message = "El campo debe no contener caracteres especiales")
    private String comments;

    @Pattern(regexp = "^[0-9]*$", message = "El campo debe contener solo dígitos del 0 al 9")
    private String id_transaccion_foreing;
    
    @Pattern(regexp = "^[0-9]*$", message = "El campo debe contener solo dígitos del 0 al 9")
    private String config_form;
    
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,:-]*$", message = "El campo debe tener una geoposición válida")
    private String gps_location;

}
