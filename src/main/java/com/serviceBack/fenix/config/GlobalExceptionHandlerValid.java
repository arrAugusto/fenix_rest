/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.config;

import com.serviceBack.fenix.Utils.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    String fieldName = error.getField();
                    String rejectedValue = String.valueOf(error.getRejectedValue());
                    return String.format("Campo '%s': valor rechazado [%s] - %s", fieldName, rejectedValue, error.getDefaultMessage());
                })
                .collect(Collectors.toList());

        ResponseService tr = new ResponseService();
        tr.setCodeResponse("09");
        tr.setCodeResponse("EL OBJETO ENVIADO NO CUMPLE CON EL FORMATO ESTABLECIDO");
        tr.setData(ResponseEntity.badRequest().body("Error de validación: " + String.join(", ", errorMessages)).toString());
        return tr;
    }
}
