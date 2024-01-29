/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.generateJWT;

import java.security.SecureRandom;

/**
 *
 * @author agr12
 */
public class StrSessiones {

    private static final int TOKEN_LENGTH = 32; // Longitud del token en bytes

    public static String generateSessionId() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(token);

        // Convierte los bytes a una cadena hexadecimal
        StringBuilder sb = new StringBuilder();
        for (byte b : token) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
