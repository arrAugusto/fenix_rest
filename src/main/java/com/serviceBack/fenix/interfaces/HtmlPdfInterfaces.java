/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.Comprobante;

/**
 * Interfaz para la generación de PDFs a partir de HTML.
 */
public interface HtmlPdfInterfaces {

    /**
     * Genera un PDF a partir de contenido HTML.
     *
     * @return Un array de bytes que contiene el PDF generado.
     */
    public ResponseService getPDFTransaction(String idTransaccion);
    
    public byte[] view_pdfGenerated(String validator);

    public Comprobante getDataPDF(String idTransaccion, String estado);
}
