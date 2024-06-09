/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

import java.util.List;
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
    private String id_bodega_afiliada;
    private String id_group_view;
    private String id_get_formulario;
    private String type_input;
    private String tag;
    private String label;
    private String icon;
    private String size;
    private boolean required;
    private boolean disabled;
    private String pattern;
    private String visible;
    private String read_only;
    private String editable;
    private String id_icon;
    private String type;
    private String estado;
    private List<Options_view_kimbo> options_view_kimbo;

}
