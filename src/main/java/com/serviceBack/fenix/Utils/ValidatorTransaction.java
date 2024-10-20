package com.serviceBack.fenix.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class ValidatorTransaction {

    private static final int DESIRED_LENGTH = 128;

    /**
     * Genera un código único robusto de 128 caracteres basado en un UUID, utilizando SHA-512 y codificación Base64 URL-safe.
     * 
     * @return Un código de 128 caracteres seguro para URLs y altamente robusto.
     */
    public String generateUniqueCode() {
        try {
            // Generar un UUID como entrada
            String uuid = UUID.randomUUID().toString().replace("-", "");

            // Usar SHA-512 para generar el hash basado en el UUID
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = md.digest(uuid.getBytes());

            // Convertir el hash a Base64 URL-safe
            String base64Hash = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

            // Asegurar que el código tenga exactamente 128 caracteres
            if (base64Hash.length() > DESIRED_LENGTH) {
                return base64Hash.substring(0, DESIRED_LENGTH);
            } else if (base64Hash.length() < DESIRED_LENGTH) {
                // Repetir el hash para asegurar que tenga 128 caracteres
                StringBuilder extendedHash = new StringBuilder(base64Hash);
                while (extendedHash.length() < DESIRED_LENGTH) {
                    extendedHash.append(base64Hash);
                }
                return extendedHash.substring(0, DESIRED_LENGTH);
            }

            return base64Hash;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
