package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.models.GetSession;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.Usuarios;
import java.util.List;

public interface UsuariosInterfaces {

	public List<GetSession> startSession(Usuarios usuarios);
	public String createUser(NuevoUsuario nuevoUsuario);
}
