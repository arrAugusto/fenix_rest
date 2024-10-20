package com.serviceBack.fenix.Utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.serviceBack.fenix.models.Comprobante;
import com.serviceBack.fenix.models.pdf.PDF_Income_Title;
import com.serviceBack.fenix.services.TransoformGetConfig;
import commons.StoredProcedures;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

@Service
public class CreateToPDFWithPDF {

    /**
     * Genera un PDF a partir de una lista de objetos PDF_Income_Title.
     *
     * @return Un array de bytes que contiene el PDF generado.
     */
    public byte[] createToNewPDF(List<PDF_Income_Title> pdfIncomeList, String id_transaction, GenericSQL genericSQL, StoredProcedures stored) {
        StringBuilder htmlContent = new StringBuilder();

        // Inicia el documento HTML con estilos CSS en línea
        htmlContent.append(headPDF());

        // Información de cabecera
        htmlContent.append("<div style='text-align: right; line-height: 1.5; margin-left: 10px;'>")
                .append("<p style='font-size: 12px;'>NIT: 874108<br />Dirección: 24 av. 41-81, Zona 12<br />Teléfono: 2422-3000<br />Email: sistemas@kimbo.com.gt</p></div>");

        // Título principal
        htmlContent.append("<h2 style='text-align: center'>Ingreso de mercadería</h2>");

        boolean titleShown = false;

        for (PDF_Income_Title pdfIncome : pdfIncomeList) {
            if (pdfIncome.getTitleTransaction().equals("title") && !titleShown) {
                htmlContent.append("<div style='font-size: 13px;'>");

                for (PDF_Income_Title.Detail detail : pdfIncome.getDetails()) {
                    String clave = detail.getClave();

                    if (clave.equalsIgnoreCase("cliente_id") || clave.equalsIgnoreCase("id_bodega") || clave.equalsIgnoreCase("codigo_transaccion")) {
                        continue;
                    }

                    String claveFormateada = clave.replace("_", " ");
                    htmlContent.append("<strong>").append(claveFormateada.substring(0, 1).toUpperCase())
                            .append(claveFormateada.substring(1))
                            .append(":</strong> ").append(detail.getTextValue()).append("<br />");
                }
                htmlContent.append("</div>");
                titleShown = true;
            }

            if (!pdfIncome.getTitleTransaction().equals("title")) {
                htmlContent.append("<table class='table' style='width:100%; border-collapse:collapse; border:1px solid black;'>")
                        .append("<thead><tr><th style='border:1px solid black; padding:8px; text-align:left;'>DESCRIPCIÓN DE MERCADERÍA</th>")
                        .append("<th style='border:1px solid black; padding:8px; text-align:center;'>CANTIDAD</th></tr></thead><tbody>");

                StringBuilder descripcionUnificada = new StringBuilder();
                String bultosProducto = "";

                for (PDF_Income_Title.Detail detail : pdfIncome.getDetails()) {
                    String clave = detail.getClave().replace("_", " ");
                    StringBuilder claveConMayuscula = new StringBuilder();
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

                    if (detail.getClave().equalsIgnoreCase("bultos_producto")) {
                        bultosProducto = detail.getTextValue();
                    } else {
                        descripcionUnificada.append(claveConMayuscula.toString()).append(": ").append(detail.getTextValue()).append(", ");
                    }
                }

                if (descripcionUnificada.length() > 2) {
                    descripcionUnificada.setLength(descripcionUnificada.length() - 2);
                }

                htmlContent.append("<tr><td style='border:1px solid black; padding:8px; font-size: 13px;'>").append(descripcionUnificada.toString()).append("</td>")
                        .append("<td style='border:1px solid black; padding:8px; text-align:center;'>").append(bultosProducto).append("</td></tr>");

                htmlContent.append("</tbody></table>");
            }
        }

        String bultos = "";

        for (PDF_Income_Title pdfIncome : pdfIncomeList) {
            if (pdfIncome.getTitleTransaction().equals("title")) {
                for (PDF_Income_Title.Detail detail : pdfIncome.getDetails()) {
                    String clave = detail.getClave();
                    if (clave.equals("bultos")) {
                        double bultosDecimal = Double.parseDouble(detail.getTextValue());
                        bultos = String.valueOf((int) bultosDecimal);
                    }
                }
            }
        }

        htmlContent.append("<div class='invoice-summary'><p>Total de Bultos: ").append(bultos).append("</p></div>")
                .append("<div class='footer'><p>Este documento fue generado por Kimbo - Sistema de Gestión de Inventarios, con firma electrónica de validez legal.</p>")
                .append("<p>Firma Electrónica: ABC123XYZ456FIRMAFICTICIA</p></div></div></body></html>");

        ValidatorTransaction validator = new ValidatorTransaction();
        Object[] params = {id_transaction, validator.generateUniqueCode(), htmlContent.toString()};
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

    public byte[] createPDFWithHTML(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null); // Usar directamente el HTML que viene como parámetro
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Genera un PDF con un mensaje de error.
     *
     * @param tituloError El título del mensaje de error.
     * @param mensajeError El contenido del mensaje de error.
     * @return Un array de bytes que contiene el PDF con el error.
     */
    public byte[] generateErrorPDF(String tituloError, String mensajeError) {
        // Construcción del contenido HTML
        String htmlContent = "<html><head><meta charset='UTF-8' /><style>"
                + "body{font-family:Arial,sans-serif;margin:0;padding:50px;text-align:center;background-color:#f8f8f8;}"
                + ".container{padding:20px;background:#ffffff;border-radius:8px;}"
                + "h1{color:#e74c3c;}"
                + "p{color:#555;}"
                + "</style></head><body><div class='container'>"
                + "<h1>" + tituloError + "</h1>"
                + "<p>" + mensajeError + "</p>"
                + "<p>Por favor, verifica la información e intenta nuevamente.</p>"
                + "</div></body></html>";

        // Genera el PDF a partir del contenido HTML
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            // Validación del HTML para depurar
            System.out.println("Generando PDF con el siguiente HTML:\n" + htmlContent);

            // Configuración del builder
            builder.withHtmlContent(htmlContent, null);  // HTML que se convertirá a PDF
            builder.toStream(outputStream);              // Salida del PDF en un array de bytes
            builder.run();                               // Ejecuta la construcción del PDF

            // Retorna los bytes generados del PDF
            return outputStream.toByteArray();

        } catch (IOException e) {
            // Manejo de errores de entrada/salida
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // Manejo de cualquier otro tipo de excepción
            e.printStackTrace();
            return null;
        }
    }

    public String headPDF() {
        return "<html><head><meta charset='UTF-8' /><style>"
                + "body{font-family:Arial,sans-serif;margin:0;padding:5px 10px;}"
                + ".container{padding:5px;background:#ffffff;border-radius:8px;position:relative;}"
                + ".header{text-align:center;margin-bottom:3px;}"
                + ".invoice-title{font-size:18px;text-transform:uppercase;margin-top:10px;margin-bottom:3px;}"
                + ".info-container{padding:8px;border:1px solid #ddd;margin-top:5px;}"
                + ".info-item{margin-bottom:4px;}"
                + ".info-item span{font-weight:bold;}"
                + ".table{width:100%;border-collapse:collapse;margin-top:5px;font-size:11px;border:1px solid #ddd;}"
                + ".table th,.table td{padding:6px;border:1px solid #ddd;text-align:left;}"
                + ".table tbody tr{margin-bottom:4px;}"
                + ".invoice-summary{text-align:right;margin-top:5px;font-weight:bold;font-size:13px;}"
                + ".footer{margin-top:10px;text-align:center;color:#aaa;font-size:9px;}</style></head><body><div class='container'>";
    }

}
