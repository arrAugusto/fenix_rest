/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author agr12
 */
@Data
@ToString
public class AuthTransaction {
    
    private String idTransaction;
    private String moduloFirma;

    @JsonIgnore
    private String auth_transaccion;
    @JsonIgnore
    private String usuario_firma;

}
