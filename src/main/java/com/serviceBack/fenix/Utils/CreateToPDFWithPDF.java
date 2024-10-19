package com.serviceBack.fenix.Utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.serviceBack.fenix.models.pdf.PDF_Income_Title;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CreateToPDFWithPDF {

    /**
     * Genera un PDF a partir de una lista de objetos PDF_Income_Title.
     *
     * @return Un array de bytes que contiene el PDF generado.
     */
    public byte[] createToPDFWithPDF(List<PDF_Income_Title> pdfIncomeList) {
        StringBuilder htmlContent = new StringBuilder();

        // Inicia el documento HTML
        htmlContent.append("<html>\n<head>\n<meta charset='UTF-8' />\n<style>\n")
                .append("body{font-family:Arial,sans-serif;margin:0;padding:20px;}\n")
                .append(".container{padding:20px;background:#ffffff;border-radius:8px;box-shadow:0 0 15px rgba(0,0,0,0.1);position:relative;}\n")
                .append(".header{text-align:center;margin-bottom:10px;}\n")
                .append(".invoice-title{font-size:26px;text-transform:uppercase;color:#4CAF50;margin-top:50px;margin-bottom:10px;}\n")
                .append(".info-container{padding:15px;border:1px solid #ddd;background-color:#f9f9f9;margin-top:20px;}\n")
                .append(".info-item{margin-bottom:8px;}\n")
                .append(".info-item span{font-weight:bold;}\n")
                .append(".table{width:100%;border-collapse:collapse;margin-top:10px;font-size:14px;border:1px solid #ddd;}\n")
                .append(".table th,.table td{padding:8px;border:1px solid #ddd;text-align:left;}\n")
                .append(".table th{background-color:#4CAF50;color:white;}\n")
                .append(".table tbody tr:nth-child(even){background-color:#f9f9f9;}\n")
                .append(".invoice-summary{text-align:right;margin-top:15px;font-weight:bold;font-size:16px;}\n")
                .append(".footer{margin-top:30px;text-align:center;color:#aaa;font-size:12px;}\n")
                .append("</style>\n</head>\n<body>\n<div class='container'>\n");

// Aquí empieza el contenido en el <body>
        htmlContent.append("<div style='text-align: right; line-height: 1.5; margin-left: 10px;'>\n") // Corregido "right"
                .append("<p style='font-size: 12px;'>\n")
                .append("NIT: 874108<br />\n")
                .append("Dirección: 24 av. 41-81, Zona 12<br />\n")
                .append("Teléfono: 2422-3000<br />\n")
                .append("Email: sistemas@kimbo.com.gt\n")
                .append("</p>\n</div>\n");

        htmlContent.append("<h2 style='text-align: center'>Ingreso de mercadería</h2>\n");

        for (PDF_Income_Title pdfIncome : pdfIncomeList) {
            // Mostrar el bloque de título sólo una vez
            if (pdfIncome.getTitleTransaction().equals("title")) {
                htmlContent.append("<div style='font-family: Arial, sans-serif; font-size: 12px; line-height: 1.2;'>\n")
                        .append("<div><strong>Empresa&#160;:</strong>&#160;GRUPO TLA GUATEMALA, S.A.</div>\n")
                        .append("<div><strong>Nit&#160;:</strong>&#160;49475428</div>\n")
                        .append("<div><strong>Bodega No.&#160;:</strong>&#160;2</div>\n")
                        .append("<div><strong>Elaborado por&#160;:</strong>&#160;ANDERSON DANIEL SANTOS CASTRO</div>\n")
                        .append("<div><strong>Descargado por&#160;:</strong>&#160;JOSE NOLBERTO LOPEZ TUN</div>\n")
                        .append("</div>\n");

            }

// Si el título de la transacción no es "title", procesar el contenido
if (!pdfIncome.getTitleTransaction().equals("title")) {
    htmlContent.append("<table style='width:100%; border-collapse:collapse; border:1px solid black;'>\n")
            .append("<thead>\n<tr>\n")
            .append("<th style='border:1px solid black; padding:8px; text-align:left;'>DESCRIPCIÓN DE MERCADERÍA</th>\n")
            .append("<th style='border:1px solid black; padding:8px; text-align:center;'>CANTIDAD</th>\n")
            .append("</tr>\n</thead>\n<tbody>\n");

    // Iterar sobre la lista de detalles y agregar los valores al HTML
    for (PDF_Income_Title.Detail detail : pdfIncome.getDetails()) {
        // Reemplazar guiones bajos por espacios y convertir a mayúscula la primera letra de cada palabra
        String clave = detail.getClave().replace("_", " ");
        StringBuilder claveConMayuscula = new StringBuilder();

        boolean isNewWord = true;
        for (char c : clave.toCharArray()) {
            if (isNewWord && Character.isLetter(c)) {
                claveConMayuscula.append(Character.toUpperCase(c));  // Primera letra en mayúscula
                isNewWord = false;
            } else {
                claveConMayuscula.append(c);
            }
            if (c == ' ') {
                isNewWord = true;  // Iniciar una nueva palabra cuando se encuentra un espacio
            }
        }

        // Agregar la clave modificada y los detalles a la tabla HTML
        htmlContent.append("<tr>\n")
                .append("<td style='border:1px solid black; padding:8px;'>").append(claveConMayuscula.toString()).append("</td>\n")
                .append("<td style='border:1px solid black; padding:8px; text-align:center;'>").append(detail.getTextValue()).append("</td>\n")
                .append("</tr>\n");
    }

    htmlContent.append("</tbody>\n</table>\n");

    // Total de bultos ingresados
    htmlContent.append("<div style='text-align:right; font-weight:bold; margin-top:10px;'>\n")
            .append("<p>TOTAL DE BULTOS INGRESADOS: 542</p>\n") // El total debe ser dinámico si es necesario
            .append("</div>\n<hr />\n");
}


        }

        // Cierra el contenido del HTML
        htmlContent.append("<div class='invoice-summary'>\n<p>Total de Bultos: 25</p>\n</div>\n")
                .append("<div class='footer'>\n")
                .append("<p>Este documento fue generado por Kimbo - Sistema de Gestión de Inventarios, con firma electrónica de validez legal.</p>\n")
                .append("<p>Firma Electrónica: ABC123XYZ456FIRMAFICTICIA</p>\n")
                .append("</div>\n</div>\n</body>\n</html>");

        // Imprimir el HTML generado para depuración
        System.out.println(htmlContent.toString());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent.toString(), null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
