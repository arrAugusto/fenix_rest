package com.serviceBack.fenix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.DetallesIngreso;
import com.serviceBack.fenix.models.GetDetalleIngreso;
import com.serviceBack.fenix.models.ItemsFail;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin("*")
@RequestMapping("/ingresos")
public class IngresoControllers {

    @Autowired
    private IngresosInterfaces service;

    @PostMapping("/crearte_ingreso")//Iniciar una nueva session de usuario
    public ResponseService createIngreso(@RequestBody @Valid Ingresos ingresos) {
        return service.createIngresos(ingresos);
    }

    @PostMapping("/items")//Iniciar una nueva session de usuario
    public ItemsFail crearOTPCode(@RequestBody @Valid DetallesIngreso detalles) {
        return service.crearItems(detalles);
    }

    @GetMapping("/items/{idTransaccion}")//Iniciar una nueva session de usuario
    public GetDetalleIngreso getItemsIng(@PathVariable @Valid @Pattern(regexp = "^[0-9]+$", message = "El campo debe no contener caracteres especiales") String idTransaccion) {
        return service.getItems(idTransaccion);
    }
}
