/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.controllers;

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
    @GetMapping(value = "/html", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadHtmlPdf() {
        byte[] pdfData = htmlPdfService.generatePdfFromHtml();

        if (pdfData != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(pdfData);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=cliente_augusto_gomez.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } else {
            return null;
        }
    }
}
