/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;


public class Product {

    private int idItem;
    private int idUser;
    private String user;
    private int bultosProd;
    private String descriptionProd;
    private String typeProd;
    private String status;

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getBultosProd() {
        return bultosProd;
    }

    public void setBultosProd(int bultosProd) {
        this.bultosProd = bultosProd;
    }

    public String getDescriptionProd() {
        return descriptionProd;
    }

    public void setDescriptionProd(String descriptionProd) {
        this.descriptionProd = descriptionProd;
    }

    public String getTypeProd() {
        return typeProd;
    }

    public void setTypeProd(String typeProd) {
        this.typeProd = typeProd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
