/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models.ingresos;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @author agr12
 */

@Data
@ToString

public class IngresosPendientes {

    private String id_transaccion;
    private String numeroFactura;
    private String fecha;
    private int bultos;
    private double valor;
    private String estado;

}
