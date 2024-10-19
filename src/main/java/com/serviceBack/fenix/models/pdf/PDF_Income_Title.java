package com.serviceBack.fenix.models.pdf;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * Clase que representa un título de ingresos en un PDF, junto con una lista de detalles.
 */
@Data
public class PDF_Income_Title {

    private String titleTransaction;

    // Cambia Detail de un solo objeto a una lista
    private List<Detail> details = new ArrayList<>();

    @Data
    public static class Detail {

        private String clave;
        private String textValue;
    }
}
