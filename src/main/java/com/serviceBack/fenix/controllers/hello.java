package com.serviceBack.fenix.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class hello {
	
	@GetMapping("/helloW")
	
	public String helloWord() {
		return "{\r\n"
				+ "  \"nombre\":\"Jonh Doe\",\r\n"
				+ "  \"profesion\":\"Programador\",\r\n"
				+ "  \"edad\":25,\r\n"
				+ "  \"lenguajes\":[\"PHP\",\"Javascript\",\"Dart\"],\r\n"
				+ "  \"disponibilidadParaViajar\":true,\r\n"
				+ "  \"rangoProfesional\": {\r\n"
				+ "      \"aniosDeExperiencia\": 12,\r\n"
				+ "      \"nivel\": \"Senior\"\r\n"
				+ "  }\r\n"
				+ "}";
	}
}
