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
        // Contenido HTML actualizado con ajustes en el tamaño del icono y optimización para una sola hoja
        String htmlContent = "<html>"
                + "<head>"
                + "<meta charset='UTF-8' />"
                + "<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css' />"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }"
                + ".container { padding: 20px; background: #ffffff; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.1); position: relative; }"
                + ".header { text-align: center; margin-bottom: 10px; position: relative; }" // Reducir el margen inferior de la cabecera
                + ".invoice-title { font-size: 26px; text-transform: uppercase; color: #4CAF50; margin-top: 50px; margin-bottom: 10px; }" // Reducir el margen inferior del título
                + ".icon-corner { position: absolute; top: -10px; right: 20px; width: 80px; height: 80px; }" // Icono más grande pero no excesivo
                + ".info-container { display: flex; flex-wrap: wrap; padding: 15px; border: 1px solid #ddd; background-color: #f9f9f9; margin-top: 20px; }" // Reducir márgenes y paddings del contenedor
                + ".info-left, .info-right { width: 50%; padding: 5px; box-sizing: border-box; }" // Ajustar padding de columnas
                + ".info-item { margin-bottom: 8px; }" // Reducir separación entre items
                + ".info-item span { font-weight: bold; }"
                + ".info-full-width { width: 100%; }"
                + ".table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 14px; border: 1px solid #ddd; }" // Reducir margen superior de la tabla
                + ".table th, .table td { padding: 8px; border: 1px solid #ddd; text-align: left; }" // Reducir padding de celdas
                + ".table th { background-color: #4CAF50; color: white; }"
                + ".table tbody tr:nth-child(even) { background-color: #f9f9f9; }"
                + ".invoice-summary { text-align: right; margin-top: 15px; font-weight: bold; font-size: 16px; }" // Ajustar márgenes de la sección de resumen
                + ".footer { margin-top: 30px; text-align: center; color: #aaa; font-size: 12px; }" // Reducir margen superior del pie de página
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "   <!-- Icono en la esquina superior derecha con tamaño ajustado -->"
                + "   <img src='https://cdn-icons-png.flaticon.com/512/5310/5310773.png' alt='Icono' class='icon-corner' />"
                + "   <div class='header'>"
                + "       <h1 class='invoice-title'><img src='https://cdn-icons-png.flaticon.com/512/190/190411.png' style='width: 24px; height: 24px; margin-right: 10px;' alt='Success' /> Transacción de Ingreso</h1>"
                + "   </div>"
                + "   <!-- Contenedor principal dividido en dos columnas -->"
                + "   <div class='info-container'>"
                + "       <!-- Columna izquierda -->"
                + "       <div class='info-left'>"
                + "           <div class='info-item'><span>Proveedor:</span> Proveedor Genérico</div>"
                + "           <div class='info-item'><span>Número de NIT:</span> 123456789</div>"
                + "           <div class='info-item'><span>Tipo de Identificación:</span> Cédula</div>"
                + "           <div class='info-item'><span>Almacén:</span> Almacén Central</div>"
                + "           <div class='info-item'><span>Fecha de Documento:</span> 08/10/2024</div>"
                + "       </div>"
                + "       <!-- Columna derecha -->"
                + "       <div class='info-right'>"
                + "           <div class='info-item'><span>Número de Documento:</span> INV-001</div>"
                + "           <div class='info-item'><span>Cantidad de Bultos:</span> 25</div>"
                + "           <div class='info-item'><span>Producto Perecedero:</span> No</div>"
                + "           <div class='info-item info-full-width'><span>Código de Transacción:</span> TX-987654</div>"
                + "       </div>"
                + "   </div>"
                + "   <!-- Tabla de detalles con estilo de Bulma -->"
                + "   <table class='table is-striped is-bordered'>"
                + "       <thead>"
                + "           <tr>"
                + "               <th>Descripción</th>"
                + "               <th>Detalle</th>"
                + "           </tr>"
                + "       </thead>"
                + "       <tbody>"
                + "           <tr>"
                + "               <td>Código de Transacción</td>"
                + "               <td>TX-987654</td>"
                + "           </tr>"
                + "           <tr>"
                + "               <td>Nombre del Proveedor</td>"
                + "               <td>Proveedor Genérico</td>"
                + "           </tr>"
                + "           <tr>"
                + "               <td>Fecha de Documento</td>"
                + "               <td>08/10/2024</td>"
                + "           </tr>"
                + "       </tbody>"
                + "   </table>"
                + "   <div class='invoice-summary'>"
                + "       <p>Total de Bultos: 25</p>"
                + "   </div>"
                + "   <div class='footer'>"
                + "       <p>Gracias por hacer negocios con nosotros</p>"
                + "   </div>"
                + "</div>"
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
