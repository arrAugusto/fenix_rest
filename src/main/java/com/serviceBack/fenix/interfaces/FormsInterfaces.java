package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.GetForms;
import com.serviceBack.fenix.models.SideNav;
import java.util.List;

public interface FormsInterfaces {
    public List<GetForms>  FormsService(String id);
    public List<SideNav> SideNavService();
}
