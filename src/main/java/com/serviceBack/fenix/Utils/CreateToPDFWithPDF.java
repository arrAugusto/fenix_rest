package com.serviceBack.fenix.Utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.serviceBack.fenix.models.pdf.PDF_Income_Title;
import commons.StoredProcedures;
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
    public byte[] createToPDFWithPDF(List< PDF_Income_Title> pdfIncomeList, String id_transaction, GenericSQL genericSQL, StoredProcedures stored) {
        System.out.println("init>>");
        StringBuilder htmlContent = new StringBuilder();

        // Inicia el documento HTML con estilos CSS en línea
        htmlContent.append("<html>\n<head>\n<meta charset='UTF-8' />\n<style>\n").append("body{font-family:Arial,sans-serif;margin:0;padding:5px 10px;}\n") // Márgenes laterales muy reducidos
                .append(".container{padding:5px;background:#ffffff;border-radius:8px;position:relative;}\n").append(".header{text-align:center;margin-bottom:3px;}\n").append(".invoice-title{font-size:18px;text-transform:uppercase;margin-top:10px;margin-bottom:3px;}\n").append(".info-container{padding:8px;border:1px solid #ddd;margin-top:5px;}\n").append(".info-item{margin-bottom:4px;}\n").append(".info-item span{font-weight:bold;}\n").append(".table{width:100%;border-collapse:collapse;margin-top:5px;font-size:11px;border:1px solid #ddd;}\n").append(".table th,.table td{padding:6px;border:1px solid #ddd;text-align:left;}\n").append(".table tbody tr{margin-bottom:4px;}\n") // Márgenes entre filas muy reducidos
                .append(".invoice-summary{text-align:right;margin-top:5px;font-weight:bold;font-size:13px;}\n").append(".footer{margin-top:10px;text-align:center;color:#aaa;font-size:9px;}\n").append("</style>\n</head>\n<body>\n<div class='container'>\n");

        // Información de cabecera
        htmlContent.append("<div style='text-align: right; line-height: 1.5; margin-left: 10px;'>\n").append("<p style='font-size: 12px;'>\n").append("NIT: 874108<br />\n").append("Dirección: 24 av. 41-81, Zona 12<br />\n").append("Teléfono: 2422-3000<br />\n").append("Email: sistemas@kimbo.com.gt\n").append("</p>\n</div>\n");

        // Título principal
        htmlContent.append("<h2 style='text-align: center'>Ingreso de mercadería</h2>\n");

        boolean titleShown = false; // Controlar la aparición del título

        // Procesamiento de las transacciones en pdfIncomeList
        for (PDF_Income_Title pdfIncome : pdfIncomeList) {
            // Mostrar el bloque de título solo una vez
            if (pdfIncome.getTitleTransaction().equals("title") && !titleShown) {
                // Recorrer los detalles y generar el contenido de cabecera basado en las claves
                htmlContent.append("<div style='font-size: 13px;'>\n"); // Reducir el tamaño de la fuente

                for (PDF_Income_Title.Detail detail : pdfIncome.getDetails()) {
                    String clave = detail.getClave();

                    // Omitir 'cliente_id', 'id_bodega', y 'codigo_transaccion'
                    if (clave.equalsIgnoreCase("cliente_id") || clave.equalsIgnoreCase("id_bodega") || clave.equalsIgnoreCase("codigo_transaccion")) {
                        continue;
                    }

                    // Formatear la clave para mostrarla adecuadamente
                    String claveFormateada = clave.replace("_", " ");
                    htmlContent.append("<strong>").append(claveFormateada.substring(0, 1).toUpperCase()) // Primera letra en mayúscula
                            .append(claveFormateada.substring(1)) // El resto de la clave en minúscula
                            .append(":</strong> ").append(detail.getTextValue()).append("<br />\n");
                }
                htmlContent.append("</div>\n"); // Cerrar el div con el tamaño de fuente reducido
                titleShown = true; // Marcar que el título ya fue mostrado
            }

            // Si el título de la transacción no es "title", procesar el contenido
            if (!pdfIncome.getTitleTransaction().equals("title")) {
                htmlContent.append("<table class='table' style='width:100%; border-collapse:collapse; border:1px solid black;'>\n").append("<thead>\n<tr>\n").append("<th style='border:1px solid black; padding:8px; text-align:left;'>DESCRIPCIÓN DE MERCADERÍA</th>\n").append("<th style='border:1px solid black; padding:8px; text-align:center;'>CANTIDAD</th>\n").append("</tr>\n</thead>\n<tbody>\n");

                // Unificar todos los detalles en una sola celda
                StringBuilder descripcionUnificada = new StringBuilder();
                String bultosProducto = ""; // Variable para almacenar el valor de bultos

                for (PDF_Income_Title.Detail detail : pdfIncome.getDetails()) {
                    String clave = detail.getClave().replace("_", " ");
                    StringBuilder claveConMayuscula = new StringBuilder();

                    // Convertir la primera letra de cada palabra a mayúscula
                    boolean isNewWord = true;
                    for (char c : clave.toCharArray()) {
                        if (isNewWord && Character.isLetter(c)) {
                            claveConMayuscula.append(Character.toUpperCase(c));
                            isNewWord = false;
                        } else {
                            claveConMayuscula.append(c);
                        }
                        if (c == ' ') {
                            isNewWord = true;
                        }
                    }

                    // Verificar si es el detalle de "bultos_producto"
                    if (detail.getClave().equalsIgnoreCase("bultos_producto")) {
                        bultosProducto = detail.getTextValue(); // Asignar el valor a la variable
                    } else {
                        // Concatenar el detalle a la descripción unificada
                        descripcionUnificada.append(claveConMayuscula.toString()).append(": ").append(detail.getTextValue()).append(", ");
                    }
                }

                // Eliminar la última coma y espacio en la descripción unificada
                if (descripcionUnificada.length() > 2) {
                    descripcionUnificada.setLength(descripcionUnificada.length() - 2);
                }

                // Agregar la descripción unificada y la columna de bultos producto
                htmlContent.append("<tr>\n").append("<td style='border:1px solid black; padding:8px; font-size: 13px; '>").append(descripcionUnificada.toString()).append("</td>\n").append("<td style='border:1px solid black; padding:8px; text-align:center;'>").append(bultosProducto).append("</td>\n").append("</tr>\n");

                htmlContent.append("</tbody>\n</table>\n");

            }
        }

        String bultos = "";
        // Procesamiento de las transacciones en pdfIncomeList
        for (PDF_Income_Title pdfIncome : pdfIncomeList) {
            // Mostrar el bloque de título solo una vez
            if (pdfIncome.getTitleTransaction().equals("title")) {
                // Recorrer los detalles y generar el contenido de cabecera basado en las claves
                for (PDF_Income_Title.Detail detail : pdfIncome.getDetails()) {
                    String clave = detail.getClave();
                    System.out.println("detail> " + detail.getTextValue());
                    if (clave.equals("bultos")) {
                        // Convertir el valor a un número decimal, luego a entero
                        double bultosDecimal = Double.parseDouble(detail.getTextValue());
                        bultos = String.valueOf((int) bultosDecimal); // Convertir a entero y luego a cadena
                        System.out.println("Total bultos encontrado: " + bultos); // Para verificar si el valor se está asignando
                    }
                }
            }
        }

        // Mostrar el total de bultos extraído
        htmlContent.append("<div class='invoice-summary'>\n<p>Total de Bultos: ").append(bultos).append("</p>\n</div>\n").append("<div class='footer'>\n").append("<p>Este documento fue generado por Kimbo - Sistema de Gestión de Inventarios, con firma electrónica de validez legal.</p>\n").append("<p>Firma Electrónica: ABC123XYZ456FIRMAFICTICIA</p>\n").append("</div>\n</div>\n</body>\n</html>");

        // Imprimir el HTML generado para depuración
        System.out.println(htmlContent.toString());
        ValidatorTransaction validator = new ValidatorTransaction();
        // Creando objeto de inserción
        Object[] params = {
            id_transaction,
            validator.generateUniqueCode(),
            htmlContent.toString()
        };
        boolean isInserted = genericSQL.insert(stored.STORED_PROCEDURE_INSERT_COMPROBANTE_TRANSACCION, params);

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
