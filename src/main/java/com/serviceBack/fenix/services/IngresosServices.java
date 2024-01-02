package com.serviceBack.fenix.services;

import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.IngresosInterfaces;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.utils.ResponseService;

@Service
public class IngresosServices implements IngresosInterfaces {
    

    
    @Override
    public ResponseService createIngresos(Ingresos ingresos) {
ResponseService response = new ResponseService();
        response.setCodeResponse("00");
        response.setMessageResponse("Ingreso creado exitosamente");
        response.setData("Ok");
        return response;
    }
}
