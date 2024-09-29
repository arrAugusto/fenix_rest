package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.GetFormUser;
import com.serviceBack.fenix.models.GetForms;
import com.serviceBack.fenix.models.SideNav;
import java.util.List;

public interface FormsInterfaces {
    public List<GetForms>  MenuService(String id);
    
    public List<GetFormUser> FormUserService(String id_form, String idTransaction);
    
    public List<SideNav> SideNavService();
}
