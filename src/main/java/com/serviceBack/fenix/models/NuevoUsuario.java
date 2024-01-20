package com.serviceBack.fenix.models;

public class NuevoUsuario {

    private String nombres;
    private String apellidos;
    private String codigoEmpleado;
    private String perfil;
    private String status;
    private String usuario;
    private String secrete_pass;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSecrete_pass() {
        return secrete_pass;
    }

    public void setSecrete_pass(String secrete_pass) {
        this.secrete_pass = secrete_pass;
    }

}
