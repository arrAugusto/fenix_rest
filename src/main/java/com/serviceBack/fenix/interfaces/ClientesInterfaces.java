package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.GetNit;
import com.serviceBack.fenix.models.NuevoCliente;
import com.serviceBack.fenix.models.UpdateCliente;
import java.util.List;

public interface ClientesInterfaces {
    
    public List<GetNit> getNitService(String id_nit);
    public String newClientService(NuevoCliente nuevoCliente);
    public String updateClienteService(UpdateCliente updateCliente);
}
