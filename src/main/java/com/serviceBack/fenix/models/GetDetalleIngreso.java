/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models;

import java.util.List;

/**
 *
 * @author agr12
 */
public class GetDetalleIngreso {

    private String idTrasaccionItem;
    private String fechaOperativaItem;
    private String documentoItem;
    private String codigoQRItem;
    private int totalBultosItem;
    private float totalCifItem;
    private float totalImpuestosItem;
    private Items items;

    public String getIdTrasaccionItem() {
        return idTrasaccionItem;
    }

    public void setIdTrasaccionItem(String idTrasaccionItem) {
        this.idTrasaccionItem = idTrasaccionItem;
    }

    public String getFechaOperativaItem() {
        return fechaOperativaItem;
    }

    public void setFechaOperativaItem(String fechaOperativaItem) {
        this.fechaOperativaItem = fechaOperativaItem;
    }

    public String getDocumentoItem() {
        return documentoItem;
    }

    public void setDocumentoItem(String documentoItem) {
        this.documentoItem = documentoItem;
    }

    public String getCodigoQRItem() {
        return codigoQRItem;
    }

    public void setCodigoQRItem(String codigoQRItem) {
        this.codigoQRItem = codigoQRItem;
    }

    public int getTotalBultosItem() {
        return totalBultosItem;
    }

    public void setTotalBultosItem(int totalBultosItem) {
        this.totalBultosItem = totalBultosItem;
    }

    public float getTotalCifItem() {
        return totalCifItem;
    }

    public void setTotalCifItem(float totalCifItem) {
        this.totalCifItem = totalCifItem;
    }

    public float getTotalImpuestosItem() {
        return totalImpuestosItem;
    }

    public void setTotalImpuestosItem(float totalImpuestosItem) {
        this.totalImpuestosItem = totalImpuestosItem;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

}
