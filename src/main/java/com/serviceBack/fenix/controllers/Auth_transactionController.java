/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.controllers;

import com.serviceBack.fenix.interfaces.AuthTransactionInterface;
import com.serviceBack.fenix.models.AuthTransaction;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/auth_transaction")
public class Auth_transactionController {

    @Autowired
    private AuthTransactionInterface service;

    @PostMapping("/auth_firma")//Iniciar una nueva session de usuario
    public String createIngreso(@RequestBody @Valid AuthTransaction authTransaction) {
        service.authTransaction(authTransaction);
        return null;
    }

}
