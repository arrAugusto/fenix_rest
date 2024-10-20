/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.controllers;

import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.interfaces.HtmlPdfInterfaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author agr12
 */
@RestController
@RequestMapping("/pdf")
public class HtmlPdfController {

    @Autowired
    private HtmlPdfInterfaces htmlPdfService;

    /**
     * Endpoint para descargar un PDF generado a partir de HTML con CSS.
     *
     * @return ResponseEntity con el archivo PDF.
     */
    @GetMapping(value = "/get_pdf/{param}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadHtmlPdf(
            @PathVariable("param") @Valid @Pattern(regexp = "^[a-zA-Z0-9\\-_]+$", message = "El parámetro solo debe contener letras y números") String validadorComprobante) {
        byte[] pdfData = htmlPdfService.view_pdfGenerated(validadorComprobante);
        
        if (pdfData != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(pdfData);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=" + validadorComprobante + ".pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/create_PDF/{idTransaccion}")//Iniciar una nueva session de usuario
    public ResponseService getItemsIng(@PathVariable @Valid @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales") String idTransaccion) {
        return htmlPdfService.getPDFTransaction(idTransaccion);
    }
}