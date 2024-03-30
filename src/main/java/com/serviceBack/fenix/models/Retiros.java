package com.serviceBack.fenix.models;

import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Retiros {
    @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales")
    private String idTransaccion;
}
