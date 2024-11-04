package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.GetSession;
import com.serviceBack.fenix.models.NuevoUsuario;
import com.serviceBack.fenix.models.ResetPasswordRequest;
import com.serviceBack.fenix.models.UpdateUserRequest;
import com.serviceBack.fenix.models.Usuarios;
import java.util.List;

public interface UsuariosInterfaces {

    public ResponseService startSession(Usuarios usuarios);
    public String createUser(NuevoUsuario nuevoUsuario);
    boolean updateUserStatusOrProfile(UpdateUserRequest updateUserRequest);
    boolean resetPassword(ResetPasswordRequest resetPasswordRequest);    
    public ResponseService getAllUsers();

}
