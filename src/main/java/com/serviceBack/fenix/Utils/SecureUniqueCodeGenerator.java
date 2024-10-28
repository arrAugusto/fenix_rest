/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author agr12
 */
public class SecureUniqueCodeGenerator {

    private static long counter = 0;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SIGNATURE_LENGTH = 21;

    public static synchronized String generateUniqueCode() {
        // Obtener la fecha y hora actual
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String formattedDate = dateFormat.format(currentDate);

        // Obtener un identificador único (UUID)
        String uuid = UUID.randomUUID().toString();

        // Obtener un número de secuencia único
        long sequenceNumber = getNextSequenceNumber();

        // Concatenar los valores obtenidos
        String combinedString = formattedDate + uuid + sequenceNumber;

        // Generar el hash utilizando SHA-256
        String uniqueCode = generateSHA256Hash(combinedString);

        return uniqueCode;
    }

    private static synchronized long getNextSequenceNumber() {
        return counter++;
    }

    public static String generateSHA256Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());

            // Convertir el hash a una representación hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Genera una cadena aleatoria de 21 caracteres utilizando SecureRandom.
     *
     * @return Una firma aleatoria similar a 'FEC5FP4680GCA929ABCDE'
     */
    public String generateSecureRandomSignature() {
        StringBuilder signature = new StringBuilder(SIGNATURE_LENGTH);
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < SIGNATURE_LENGTH; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            signature.append(CHARACTERS.charAt(index));
        }

        return signature.toString();
    }

    public static String generateTransactionId() {
        // Genera un UUID
        UUID uuid = UUID.randomUUID();
        
        // Obtén la fecha y hora actual en formato 'yyyyMMddHHmmss'
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // Genera un número aleatorio adicional para mayor aleatoriedad
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        
        // Concatenar UUID, timestamp y número aleatorio
        String uniqueString = uuid.toString() + timestamp + randomNum;
        
        try {
            // Crear instancia de SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(uniqueString.getBytes(StandardCharsets.UTF_8));
            
            // Codificar en Base64, convertir a mayúsculas y eliminar caracteres especiales
            String base64Encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hash).replaceAll("[^A-Z0-9]", "").toUpperCase();
            
            // Asegurar que la longitud sea de exactamente 30 caracteres
            if (base64Encoded.length() < 30) {
                base64Encoded = String.format("%-30s", base64Encoded).replace(' ', 'X');
            } else if (base64Encoded.length() > 30) {
                base64Encoded = base64Encoded.substring(0, 30);
            }

            return base64Encoded;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar ID de transacción", e);
        }
    }
    
}
