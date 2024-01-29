/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

/**
 *
 * @author agr12
 */
public class SessionUser {

    private String jwt;
    private String strSessionId;

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
