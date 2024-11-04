package com.serviceBack.fenix.controllers;

import com.serviceBack.fenix.Utils.ResponseService;
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
import com.serviceBack.fenix.models.ResetPasswordRequest;
import com.serviceBack.fenix.models.UpdateUserRequest;
import com.serviceBack.fenix.models.Usuarios;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UserControllers {

    @Autowired
    private UsuariosInterfaces service;

    @PostMapping("/session")//Iniciar una nueva session de usuario
    public ResponseService startSession(@RequestBody Usuarios usuarios) {
        System.out.println(usuarios.getSecrete_pass());
        return service.startSession(usuarios);
    }

    @PostMapping("/create_user")//Crear un usuario nuevo
    public String createUserCtr(@RequestBody NuevoUsuario nuevoUsuario) {
        return service.createUser(nuevoUsuario);
    }

    @PutMapping("/update_profile_status")
    public ResponseEntity<String> updateUserStatusOrProfile(@RequestBody UpdateUserRequest updateUserRequest) {
        boolean success = service.updateUserStatusOrProfile(updateUserRequest);
        return success ? ResponseEntity.ok("Usuario actualizado exitosamente") : ResponseEntity.badRequest().body("Error al actualizar el usuario");
    }

    @PutMapping("/reset_password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean success = service.resetPassword(resetPasswordRequest);
        return success ? ResponseEntity.ok("Contraseña actualizada exitosamente") : ResponseEntity.badRequest().body("Error al actualizar la contraseña");
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<ResponseService> getAllUsers() {
        ResponseService response = service.getAllUsers();
        return ResponseEntity.ok(response);
    }
}
