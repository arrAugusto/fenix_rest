/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author agr12
 */
@Data
@ToString
public class Comprobante {

    private String idTransaction;
    private String validadorComprobante;
    private String fecha_creacion;
    private String url_comprobante;
    @JsonIgnore  // Ignorar este campo durante la serialización/deserialización
    private String comprobante;

}
