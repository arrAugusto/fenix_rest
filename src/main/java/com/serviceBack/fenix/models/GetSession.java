/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

/**
 *
 * @author agr12
 */
public class GetSession {

    private String user;
    private String timenow;
    private String timeExp;
    private String nombres;
    private String apellidos;
    private String perfil;
    private String id_almacen;
    private String jwt;
    private String strSessionId;

    @Override
    public String toString() {
        return "GetSession{" + "user=" + user + ", timenow=" + timenow + ", timeExp=" + timeExp + ", nombres=" + nombres + ", apellidos=" + apellidos + ", perfil=" + perfil + ", id_almacen=" + id_almacen + ", jwt=" + jwt + ", strSessionId=" + strSessionId + '}';
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTimenow() {
        return timenow;
    }

    public void setTimenow(String timenow) {
        this.timenow = timenow;
    }

    public String getTimeExp() {
        return timeExp;
    }

    public void setTimeExp(String timeExp) {
        this.timeExp = timeExp;
    }

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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getId_almacen() {
        return id_almacen;
    }

    public void setId_almacen(String id_almacen) {
        this.id_almacen = id_almacen;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getStrSessionId() {
        return strSessionId;
    }

    public void setStrSessionId(String strSessionId) {
        this.strSessionId = strSessionId;
    }

}
