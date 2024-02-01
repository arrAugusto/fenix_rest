/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 *
 * @author agr12
 */
public class QRCodeGenerator {

    public static void generateQRCode(String data, String filePath, int width, int height) {
        try {
            // Crear un objeto Map para establecer parámetros como la corrección de errores
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            // Crear el objeto BitMatrix que representa el código QR
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            // Escribir la matriz en un archivo de imagen
            Path path = FileSystems.getDefault().getPath(filePath);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            javax.imageio.ImageIO.write(image, "png", new File(path.toString()));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
