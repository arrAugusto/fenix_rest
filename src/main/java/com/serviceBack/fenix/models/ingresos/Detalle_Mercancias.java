/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models.ingresos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 *
 * @author agr12
 */
@Data
public class Detalle_Mercancias {

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String id_transaction;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private String area;

    
    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private String bultos;
    
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$", message = "El campo debe estar vacío o tener el formato 'yyyy-MM-dd HH:mm:ss'")
    private String fechaRegistro;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*\\s.'\\[\\]]*$", message = "El campo no debe contener caracteres especiales")
    private String detalle;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*]+$", message = "El campo debe no contener caracteres especiales")
    private String averias;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*]+$", message = "El campo debe no contener caracteres especiales")
    private String codigolectura;

}
