package com.serviceBack.fenix.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serviceBack.fenix.interfaces.UsuariosInterface;
import com.serviceBack.fenix.models.Usuarios;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UsuariosControllers {

	
	@Autowired
	private UsuariosInterface service;
	
	@PostMapping("/session")
	public String inicioSesion(@RequestBody Usuarios usuarios) {
		System.out.println(usuarios.getSecrete_pass());
		
		return service.iniciSession(usuarios);
	}
}
