/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.serviceBack.fenix.interfaces.HtmlPdfInterfaces;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author agr12
 */
@Service
public class HtmlPdfService implements HtmlPdfInterfaces {

    /**
     * Genera un PDF a partir de contenido HTML con CSS.
     *
     * @return Un array de bytes que contiene el PDF generado.
     */
    @Override
    public byte[] generatePdfFromHtml() {
        // Contenido HTML con estilos CSS en línea
        String htmlContent = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; }"
                + "h1 { color: #4CAF50; text-align: center; }"
                + "p { font-size: 14px; text-align: center; }"
                + ".client-info { font-size: 16px; margin-top: 20px; text-align: center; color: #000; font-weight: bold; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<h1>Reporte de Cliente</h1>"
                + "<p>Este es un informe generado dinámicamente.</p>"
                + "<div class='client-info'>Nombre cliente: Augusto Gomez</div>"
                + "</body>"
                + "</html>";

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Crear un PdfRendererBuilder y establecer el contenido HTML
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();

            // Retornar el contenido del PDF como un array de bytes
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
