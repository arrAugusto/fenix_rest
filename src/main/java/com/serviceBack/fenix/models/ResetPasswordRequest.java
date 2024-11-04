/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

import lombok.Data;

/**
 *
 * @author agr12
 */
@Data
public class ResetPasswordRequest {

    private int id;
    private String newPassword;
}
