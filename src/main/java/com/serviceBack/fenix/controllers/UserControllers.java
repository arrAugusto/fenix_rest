package com.serviceBack.fenix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviceBack.fenix.generateJWT.JwtService;
import com.serviceBack.fenix.interfaces.UsuariosInterfaces;
import com.serviceBack.fenix.models.GetSession;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UserControllers {

    @Autowired
    private UsuariosInterfaces service;

    @PostMapping("/session")//Iniciar una nueva session de usuario
    public List<GetSession> startSession(@RequestBody Usuarios usuarios) {
        System.out.println(usuarios.getSecrete_pass());
        return service.startSession(usuarios);
    }

    @PostMapping("/create_user")//Crear un usuario nuevo
    public String createUserCtr(@RequestBody NuevoUsuario nuevoUsuario) {
        return service.createUser(nuevoUsuario);
    }
}
