package com.serviceBack.fenix.services;

import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CheckIncome {

    public String validFormIncome(IncomeAndWithDrawal ingreso) {
        if (ingreso.getBultos() <= 0) {
            return "Error: Bultos debe ser mayor a 0.";
        }

        String fechaIngreso = ingreso.getFecha();
        if (fechaIngreso == null || fechaIngreso.isEmpty()) {
            return "Error: Fecha no puede estar vacía.";
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaIngreso.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (fecha.isAfter(LocalDate.now())) {
                return "Error: Fecha no puede ser mayor a hoy.";
            }
        } catch (DateTimeParseException e) {
            return "Error: Formato de fecha inválido.";
        }

        return "00";
    }
}
