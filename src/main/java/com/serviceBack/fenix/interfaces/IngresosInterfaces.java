package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.DetallesIngreso;
import com.serviceBack.fenix.models.GetDetalleIngreso;
import com.serviceBack.fenix.models.ItemsFail;
import java.util.List;

public interface IngresosInterfaces {
        //Crear un ingeso
	public ResponseService createIngresos(Ingresos ingresos);
        
        //Guardar Items
        public ItemsFail crearItems(DetallesIngreso detalles);
        
        //Mostrar Items de un ingreso
        public GetDetalleIngreso getItems(String idTransaccion);
}
