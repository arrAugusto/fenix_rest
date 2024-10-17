/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models.pdf;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author agr12
 */
@Data
public class PDF_Income_Title {
    private String titleTransaction;

    // Inicializa el campo Detail sin necesidad de @Builder.Default
    private Detail detail = new Detail();

    @Data
    public static class Detail {
        private String clave;
        private String textValue;
    }
}