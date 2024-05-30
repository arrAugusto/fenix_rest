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
public class GetForms {

    private String id;
    private String nombre;
    private String description;
    private String ayuda;
    private String status;
    private String level;
    private String fecha;
    private String grupo;
    private String url;
    private String image;


}
