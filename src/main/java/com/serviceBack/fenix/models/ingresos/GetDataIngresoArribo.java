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
public class GetDataIngresoArribo {

    private String nombre_empresa;
    private String numero_factura;
    private String fecha_ingreso_bodega;
    private String item;
    private String bultos;
    private String detalle;
    private String averias;
    private String codigo_lectura;
    private String posicion_X;
    private String posicion_Y;
    private String coordenadas;
    private String descripcion;
    private String fecha_actualizacion;
}
