/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.services;

import com.serviceBack.fenix.interfaces.FormsInterfaces;
import com.serviceBack.fenix.models.GetFormUser;
import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author agr12
 */
@Service
public class RegisterProducts {

    @Autowired
    FormsInterfaces service;

    public void registerProduct(IncomeAndWithDrawal ingreso) {
        List<GetFormUser> config = this.service.FormUserService(ingreso.getConfig_form());
        System.out.println(config.toString());
        config.forEach(item -> System.out.println("config> " + item.getSub_name_column()));
    }

}
