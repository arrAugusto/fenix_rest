package com.serviceBack.fenix.config;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordCrypt {
	
	public String CryptHashPass(String password) {
		System.out.println(password);
		// Generar un hash seguro de la contraseña
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		System.out.println("Contraseña original: " + password);
		System.out.println("Contraseña hasheada: " + hashedPassword);

		// Verificar la contraseña
		boolean result = BCrypt.checkpw("otraContraseña", hashedPassword);
		System.out.println("Verificación de contraseña: " + result);
		return hashedPassword;
	}

}
