package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.DetallesIngreso;

public interface IngresosInterfaces {

	public ResponseService createIngresos(Ingresos ingresos);
        
        public String crearItems(DetallesIngreso detalles);
}
