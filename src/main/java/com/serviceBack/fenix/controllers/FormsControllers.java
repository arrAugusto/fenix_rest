/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.controllers;

import com.serviceBack.fenix.interfaces.FormsInterfaces;
import com.serviceBack.fenix.models.GetForms;
import com.serviceBack.fenix.models.Ingresos;
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
    public List<GetForms>  getForms(@PathVariable String id) {
        return service.FormsService(id);
    }

    @PostMapping("/getSideNav")//Iniciar una nueva session de usuario
    public List<SideNav>  getSideNav() {
        return service.SideNavService();
    }
    
}
