package com.serviceBack.fenix.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serviceBack.fenix.config.PasswordCrypt;
import com.serviceBack.fenix.generateJWT.JwtService;
import com.serviceBack.fenix.interfaces.UsuariosInterfaces;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;

@Service
public class SessionUsuarios implements UsuariosInterfaces {

	@Autowired
	private JwtService jwtService;

	@Override
	public String startSession(Usuarios usuarios) {
		// TODO Auto-generated method stub

		// Verificar la contraseña
		if (usuarios.getUsuario().toUpperCase().equals("DEVAGOMEZ")
				&& BCrypt.checkpw("Cintra", "$2a$10$t/noj1Vjn1sWEM98G2aD1e5muK0FYaksyxP6xgwAk9hDAJ0OVfH.u")) {

			return "jwt: " + jwtService.generateToken(usuarios.getUsuario());

		} else {
			return "contraseña o usuario no existen";
		}

	}

	@Override
	public String createUser(NuevoUsuario nuevoUsuario) {
		PasswordCrypt passCrypt = new PasswordCrypt();
		passCrypt.CryptHashPass(nuevoUsuario.getSecrete_pass());
		return "Usuario Creado";
	}
}
