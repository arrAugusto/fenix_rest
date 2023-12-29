package com.serviceBack.fenix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.generateJWT.JwtService;
import com.serviceBack.fenix.interfaces.UsuariosInterface;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;

@Service
public class SessionUsuarios implements UsuariosInterface {

	@Autowired
	private JwtService jwtService;

	@Override
	public String iniciSession(Usuarios usuarios) {
		// TODO Auto-generated method stub
		if (usuarios.getUsuario().toUpperCase().equals("DEVAGOMEZ") && usuarios.getSecrete_pass().equals("Contra$20")) {
			return "jwt: " + jwtService.generateToken(usuarios.getUsuario());
		} else {
			return "contraseña o usuario no existen";
		}

	}

	@Override
	public String nuevoUsuario(NuevoUsuario nuevoUsuario) {
		return "nuevo usuario creado";
	}
}
