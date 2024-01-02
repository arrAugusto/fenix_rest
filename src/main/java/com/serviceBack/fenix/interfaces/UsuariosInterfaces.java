package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;

public interface UsuariosInterfaces {

	public String startSession(Usuarios usuarios);
	public String createUser(NuevoUsuario nuevoUsuario);
}
