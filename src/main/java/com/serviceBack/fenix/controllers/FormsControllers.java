/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.controllers;

import com.serviceBack.fenix.interfaces.FormsInterfaces;
import com.serviceBack.fenix.models.GetFormUser;
import com.serviceBack.fenix.models.GetForms;
import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
import com.serviceBack.fenix.models.SideNav;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author agr12
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/forms")
public class FormsControllers {

    @Autowired
    FormsInterfaces service;

    @GetMapping("/getForms/{id}")//Iniciar una nueva session de usuario
    public List<GetForms> getMenu(@PathVariable String id) {
        return service.MenuService(id);
    }

    @GetMapping("/getFormulario/{id_form}") // Iniciar una nueva sesión de usuario
    public List<GetFormUser> getFormUser(
            @PathVariable String id_form,
            @RequestParam(value = "id_transaction", required = false) String idTransaction) {

        System.out.println("ID del formulario: " + id_form);
        System.out.println("ID de transacción: " + idTransaction);

        // Realizar lógica adicional si `id_transaction` está presente
        if (idTransaction != null) {
            System.out.println("ID de transacción proporcionado: " + idTransaction);
            // Puedes agregar lógica adicional aquí, por ejemplo, validar la transacción o filtrado adicional
        }

        // Retornar la lista de formularios del servicio
        return service.FormUserService(id_form, idTransaction);
    }

    @GetMapping("/getSideNav")//Iniciar una nueva session de usuario
    public List<SideNav> getSideNav() {
        return service.SideNavService();
    }

}
