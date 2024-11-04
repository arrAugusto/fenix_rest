package com.serviceBack.fenix.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NuevoUsuario {

    private String nombres;
    private String apellidos;
    private String codigoEmpleado;
    private String perfil;
    private String status;
    private String usuario;
    private String secrete_pass;

}
