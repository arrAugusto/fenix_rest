/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models.ingresos;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author agr12
 */
@Data
@ToString
public class DetallesIngreso {

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String idTransaccion;

    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int idUsuarioOperativo;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*]+$", message = "El campo debe no contener caracteres especiales")
    private String cliente;
    private List<Items> Items;

}
