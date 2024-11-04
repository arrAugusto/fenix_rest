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

@Data
@ToString
public class GetUsuario {
    private int id;
    private String nombres;
    private String apellidos;
    private String codigoEmpleado;
    private String perfil;
    private String status;
    private String usuario;
    private String fechaCreacion;
    private String imagenPerfil;
    private int paquete;

}
