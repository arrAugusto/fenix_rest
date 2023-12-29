package com.serviceBack.fenix.services;

import org.springframework.stereotype.Service;

import com.serviceBack.fenix.interfaces.UsuariosInterface;
import com.serviceBack.fenix.models.Usuarios;

@Service
public class SessionUsuarios implements UsuariosInterface {

	@Override
	public String iniciSession(Usuarios usuarios) {
		// TODO Auto-generated method stub
		return "hola desde el service";
	}

}
