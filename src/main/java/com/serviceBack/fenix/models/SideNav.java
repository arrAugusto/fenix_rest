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
public class SideNav {

    private String id;
    private String nombre;
    private String status;
    private String description;
    private String paquete;
    private String grupo;
    private String icon;

}
