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
public class GetFormUser {

    private int id;
    private String stored_group;
    private String id_icon;
    private String type;
    private String tag;
    private String label;
    private String icon;
    private String size;
    private String required;
    private String disabled;
    private String pattern;
    private String visible;
    private String read_only;
    private String editable;
}
