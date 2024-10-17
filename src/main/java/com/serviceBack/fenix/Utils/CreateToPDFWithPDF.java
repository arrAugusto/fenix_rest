/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.Utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.serviceBack.fenix.models.pdf.PDF_Income_Title;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author agr12
 */
@Service
public class CreateToPDFWithPDF {

    /**
     * Genera un PDF a partir de una lista de objetos PDF_Income_Title.
     *
     * @return Un array de bytes que contiene el PDF generado.
     */
    public byte[] createToPDFWithPDF(List<PDF_Income_Title> pdfIncomeList) {
        // Inicializar un StringBuilder para construir el contenido HTML dinámicamente
        StringBuilder htmlContent = new StringBuilder();

        // Añadir las cabeceras HTML y CSS
        htmlContent.append("<html>")
                .append("<head>")
                .append("<meta charset='UTF-8' />")
                .append("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css' />")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }")
                .append(".container { padding: 20px; background: #ffffff; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.1); position: relative; }")
                .append(".header { text-align: center; margin-bottom: 10px; position: relative; }")
                .append(".invoice-title { font-size: 26px; text-transform: uppercase; color: #4CAF50; margin-top: 50px; margin-bottom: 10px; }")
                .append(".icon-corner { position: absolute; top: -10px; right: 20px; width: 80px; height: 80px; }")
                .append(".info-container { display: flex; flex-wrap: wrap; padding: 15px; border: 1px solid #ddd; background-color: #f9f9f9; margin-top: 20px; }")
                .append(".info-left, .info-right { width: 50%; padding: 5px; box-sizing: border-box; }")
                .append(".info-item { margin-bottom: 8px; }")
                .append(".info-item span { font-weight: bold; }")
                .append(".info-full-width { width: 100%; }")
                .append(".table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 14px; border: 1px solid #ddd; }")
                .append(".table th, .table td { padding: 8px; border: 1px solid #ddd; text-align: left; }")
                .append(".table th { background-color: #4CAF50; color: white; }")
                .append(".table tbody tr:nth-child(even) { background-color: #f9f9f9; }")
                .append(".invoice-summary { text-align: right; margin-top: 15px; font-weight: bold; font-size: 16px; }")
                .append(".footer { margin-top: 30px; text-align: center; color: #aaa; font-size: 12px; }")
                .append("</style>")
                .append("</head>")
                .append("<body>");

        // Crear el contenedor principal
        htmlContent.append("<div class='container'>");

        // Variable para controlar si ya hemos mostrado el título global
        boolean titleShown = false;

        // Ciclo para procesar las transacciones
        for (PDF_Income_Title pdfIncome : pdfIncomeList) {
            PDF_Income_Title.Detail detail = pdfIncome.getDetail();

            // Mostrar el título como texto (sin tabla)
            if ("title".equals(pdfIncome.getTitleTransaction()) && !titleShown) {
                htmlContent.append("<div>")
                        .append("<h2 style='font-size:20px;'><strong>Título de la Transacción:</strong> ").append(pdfIncome.getTitleTransaction()).append("</h2>")
                        .append("<p><strong>Clave:</strong> ").append(detail.getClave()).append("</p>")
                        .append("<p><strong>Texto:</strong> ").append(detail.getTextValue()).append("</p>")
                        .append("<p><strong>Código de Transacción:</strong> TX-987654</p>")
                        .append("<p><strong>Número de Documento:</strong> INV-001</p>")
                        .append("<p><strong>Producto Perecedero:</strong> No</p>")
                        .append("<hr />")  // Línea divisoria
                        .append("</div>");
                titleShown = true; // Marcar que el título ya fue mostrado
            } else if (!"title".equals(pdfIncome.getTitleTransaction())) {
                // Mostrar los detalles de la transacción en formato tabla
                htmlContent.append("<table class='table is-striped is-bordered'>")
                        .append("   <thead>")
                        .append("       <tr>")
                        .append("           <th>Descripción</th>")
                        .append("           <th>Detalle</th>")
                        .append("       </tr>")
                        .append("   </thead>")
                        .append("   <tbody>")
                        .append("       <tr>")
                        .append("           <td>Título de la Transacción</td>")
                        .append("           <td>").append(pdfIncome.getTitleTransaction()).append("</td>")
                        .append("       </tr>")
                        .append("       <tr>")
                        .append("           <td>Clave</td>")
                        .append("           <td>").append(detail.getClave()).append("</td>")
                        .append("       </tr>")
                        .append("       <tr>")
                        .append("           <td>Texto</td>")
                        .append("           <td>").append(detail.getTextValue()).append("</td>")
                        .append("       </tr>")
                        .append("   </tbody>")
                        .append("</table>")
                        .append("<hr />");  // Línea divisoria entre detalles
            }
        }

        // Resumen de la factura y pie de página
        htmlContent.append("   <div class='invoice-summary'>")
                .append("       <p>Total de Bultos: 25</p>")
                .append("   </div>")
                .append("   <div class='footer'>")
                .append("       <p>Gracias por hacer negocios con nosotros</p>")
                .append("   </div>")
                .append("</div>") // Cerrar el contenedor principal
                .append("</body>")
                .append("</html>");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Crear un PdfRendererBuilder y establecer el contenido HTML
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent.toString(), null);
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