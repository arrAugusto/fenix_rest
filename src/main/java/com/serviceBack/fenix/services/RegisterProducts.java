/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.services;

import com.serviceBack.fenix.interfaces.FormsInterfaces;
import com.serviceBack.fenix.models.GetFormUser;
import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
import commons.StoredProcedures;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author agr12
 */
@Service
public class RegisterProducts {

    @Autowired
    FormsInterfaces service;
    private final JdbcTemplate jdbcTemplate;
    private final StoredProcedures stored; // Nueva variable de instancia

    @Autowired
    public RegisterProducts(JdbcTemplate jdbcTemplate) {
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerProduct(IncomeAndWithDrawal ingreso) {
        List<GetFormUser> config = this.service.FormUserService(ingreso.getConfig_form());
        System.out.println(config.toString());
        //jdbcTemplate.query(stored.STORED_PROCEDURE_CALL_INSERT_INVENTORY_PRODUCT, rse)
        config.stream()
                .filter(item -> item.getSub_name_column() != null && item.getSub_name_column().equals("id_transaction_ingreso"))
                .forEach(item -> System.out.println("config> " + item.getSub_name_column()));

    }

}
