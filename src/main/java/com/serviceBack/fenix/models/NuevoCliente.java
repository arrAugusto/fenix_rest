/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

import javax.validation.constraints.Pattern;

/**
 *
 * @author agr12
 */
public class NuevoCliente {

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String nit_cui;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String tipo_doc;

    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*]+$", message = "El campo debe no contener caracteres especiales")
    private String nombre;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El campo debe no contener caracteres especiales")
    private String direccion;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El campo debe no contener un email valido")
    private String email;

    public String getNit_cui() {
        return nit_cui;
    }

    public void setNit_cui(String nit_cui) {
        this.nit_cui = nit_cui;
    }

    public String getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(String tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
