/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;
import lombok.Data;
import lombok.ToString;
/**
 *
 * @author agr12
 */
@ToString
@Data

public class GetNit {
    private int id_nit;
    private String nit;
    private String nombreEmpresa;
    private String direccion;
    private String tipoDocumento;
    private String email;
    private String fechaRegistro;
    private String estado;

}
