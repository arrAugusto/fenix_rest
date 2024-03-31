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
public class ErrorInfo {
    private String key;
    private String code;
    private String message;
}