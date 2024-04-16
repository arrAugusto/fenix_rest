package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.Detalle_Mercancias;
import com.serviceBack.fenix.models.DetallesIngreso;
import com.serviceBack.fenix.models.GetDetalleIngreso;
import com.serviceBack.fenix.models.ItemsFail;
import com.serviceBack.fenix.models.Product;
import java.util.List;

public interface IngresosInterfaces {
        //Crear un ingeso y/o retiro
	public ResponseService incomeWithdrawalService(Ingresos ingresos);
        
        //Guardar Items
        public ItemsFail incomeItemsService(DetallesIngreso detalles);
        
        //Guardar detalle de mercaderias
        public ItemsFail incomeDetailsService(Detalle_Mercancias arribo_detalles);
        
        //Mostrar Items de un ingreso
        public List<GetDetalleIngreso> getItems(String idTransaccion);
        
        //Crearte product
        public String createProduct(Product product);
}
