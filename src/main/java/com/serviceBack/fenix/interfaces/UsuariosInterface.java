package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;

public interface UsuariosInterface {

	public String iniciSession(Usuarios usuarios);
	public String nuevoUsuario(NuevoUsuario nuevoUsuario);
}
