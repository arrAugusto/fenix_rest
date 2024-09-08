/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.config;

import com.serviceBack.fenix.Utils.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandlerValid {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseService handleValidationException(MethodArgumentNotValidException ex) {
        // Obtener los mensajes de error de los campos
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    String fieldName = error.getField();
                    String rejectedValue = error.getRejectedValue() != null ? error.getRejectedValue().toString() : "null";
                    return String.format("Campo '%s': valor rechazado [%s] - %s", fieldName, rejectedValue, error.getDefaultMessage());
                })
                .collect(Collectors.toList());

        // Crear la respuesta de error
        ResponseService response = new ResponseService();
        response.setCodeResponse("09"); // Código de error de validación
        response.setMessageResponse("EL OBJETO ENVIADO NO CUMPLE CON EL FORMATO ESTABLECIDO"); // Mensaje de error
        response.setData(errorMessages); // Establecer una lista de mensajes de error

        return response;
    }
}
