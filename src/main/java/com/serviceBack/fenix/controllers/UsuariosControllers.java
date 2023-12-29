package com.serviceBack.fenix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviceBack.fenix.generateJWT.JwtService;
import com.serviceBack.fenix.interfaces.UsuariosInterface;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UsuariosControllers {

	@Autowired
	private UsuariosInterface service;

	@PostMapping("/session")//Iniciar una nueva session de usuario
	public String inicioSesion(@RequestBody Usuarios usuarios) {
		System.out.println(usuarios.getSecrete_pass());
		return service.iniciSession(usuarios);
	}
	
	@PostMapping("/crear_usuario")//Crear un usuario nuevo
	public String crearNuevoUsuario(@RequestBody NuevoUsuario nuevoUsuario) {
		return service.nuevoUsuario(nuevoUsuario);
	}	
}
