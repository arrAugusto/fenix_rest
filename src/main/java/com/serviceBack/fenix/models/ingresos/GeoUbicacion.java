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
public class GeoUbicacion {

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int p_id_arribo;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*\\s.'\\[\\]]*$", message = "El campo no debe contener caracteres especiales")
    private String p_pasicionx;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*\\s.'\\[\\]]*$", message = "El campo no debe contener caracteres especiales")
    private String p_posiciony;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*\\s.'\\[\\]]*$", message = "El campo no debe contener caracteres especiales")

    private String p_geoposicion;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*\\s.'\\[\\]]*$", message = "El campo no debe contener caracteres especiales")
    private String p_codigo_lectura;

}
