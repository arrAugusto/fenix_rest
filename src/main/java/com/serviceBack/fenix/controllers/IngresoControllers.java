package com.serviceBack.fenix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.utils.ResponseService;

@RestController
@CrossOrigin("*")
@RequestMapping("/ingresos")
public class IngresoControllers {

    @Autowired
    private IngresosInterfaces service;

    @PostMapping("/crearte_ingreso")//Iniciar una nueva session de usuario
    public ResponseService startSession(@RequestBody Ingresos ingresos) {
        return service.createIngresos(ingresos);
    }
}
