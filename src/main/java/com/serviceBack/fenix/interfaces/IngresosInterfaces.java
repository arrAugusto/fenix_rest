package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.ingresos.Detalle_Mercancias;
import com.serviceBack.fenix.models.ingresos.DetallesIngreso;
import com.serviceBack.fenix.models.ingresos.GeoUbicacion;
import com.serviceBack.fenix.models.ingresos.GetDetalleIngreso;
import com.serviceBack.fenix.models.ingresos.ItemsFail;
import com.serviceBack.fenix.models.Product;
import com.serviceBack.fenix.models.ingresos.IngresosPendientes;
import java.util.List;

public interface IngresosInterfaces {

    //Crear un ingeso y/o retiro
    public ResponseService incomeWithdrawalService(IncomeAndWithDrawal ingresos);

    //Guardar Items
    public ItemsFail incomeItemsService(DetallesIngreso detalles);

    //Guardar detalle de mercaderias
    public ItemsFail incomeDetailsService(Detalle_Mercancias arribo_detalles);

    //Guardar geoposicion
    public ItemsFail incomeGeoUbicacionService(GeoUbicacion geoUbicacion);

    //Guardar geoposicion
    public ItemsFail incomeModGeoUbicacionService(GeoUbicacion geoUbicacion);

    //Mostrar Items de un ingreso
    public List<GetDetalleIngreso> getItems(String idTransaccion);

    //Crearte product
    public String createProduct(Product product);

    //Ingresos Pendientes
    public ResponseService getIngresosPendientes();
    
}
