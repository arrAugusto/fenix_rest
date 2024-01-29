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

    private String asIdUser;
    private String user;
    private String pass;
    private String timenow;
    private String timeExp;
    private String jwt;
    private String strSessionId;

    @Override
    public String toString() {
        return "GetSession{" + "asIdUser=" + asIdUser + ", user=" + user + ", pass=" + pass + ", timenow=" + timenow + ", timeExp=" + timeExp + ", jwt=" + jwt + ", strSessionId=" + strSessionId + '}';
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

    public String getAsIdUser() {
        return asIdUser;
    }

    public void setAsIdUser(String asIdUser) {
        this.asIdUser = asIdUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

}
