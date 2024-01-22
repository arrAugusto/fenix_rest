/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 *
 * @author agr12
 */
public class DetallesIngreso {

    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String idTransaccion;
    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int idUsuarioOperativo;
    @Min(value = 0, message = "El valor debe ser igual o mayor que {value}")
    @Max(value = 100000, message = "El valor debe ser igual o menor que {value}")
    private int totalBultos;
    @Pattern(regexp = "^[a-zA-Z0-9!#$%&/()=¿?¡_\\-:;,{}+*]+$", message = "El campo debe no contener caracteres especiales")
    private String cliente;
    private List<Items> Items;

    @Override
    public String toString() {
        return "DetallesIngreso{" + "idTransaccion=" + idTransaccion + ", totalBultos=" + totalBultos + '}';
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getIdUsuarioOperativo() {
        return idUsuarioOperativo;
    }

    public void setIdUsuarioOperativo(int idUsuarioOperativo) {
        this.idUsuarioOperativo = idUsuarioOperativo;
    }

    public int getTotalBultos() {
        return totalBultos;
    }

    public void setTotalBultos(int totalBultos) {
        this.totalBultos = totalBultos;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public List<Items> getItems() {
        return Items;
    }

    public void setItems(List<Items> Items) {
        this.Items = Items;
    }

}
