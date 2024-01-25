/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.controllers;

import com.serviceBack.fenix.interfaces.ClientesInterfaces;
import com.serviceBack.fenix.models.GetNit;
import com.serviceBack.fenix.models.NuevoCliente;
import com.serviceBack.fenix.models.UpdateCliente;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author agr12
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private ClientesInterfaces service;

    @GetMapping("/get_nit/{id_nit}")//GET NIT
    public List<GetNit> getNit(@PathVariable String id_nit) {
        return service.getNitService(id_nit);
    }

    @PostMapping("/new_nit")//CREAR NIT
    public String crearNit(@RequestBody @Valid NuevoCliente nuevoCliente) {
        return service.newClientService(nuevoCliente);
    }

    @PostMapping("/update_nit")//MODIFICAR NIT
    public String updateNits(@RequestBody @Valid UpdateCliente updateCliente) {
        return service.updateClienteService(updateCliente);
    }
}
