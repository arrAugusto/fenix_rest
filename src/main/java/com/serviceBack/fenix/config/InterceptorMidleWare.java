package com.serviceBack.fenix.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class InterceptorMidleWare extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// Lógica que se ejecutará antes de manejar la solicitud
		System.out.println("Interceptor: Pre-handle");

		// Leer el token JWT del encabezado de autorización
		String token = request.getHeader("Authorization");

		if (token != null && token.startsWith("Bearer ")) {
			// Elimina el prefijo "Bearer " para obtener solo el token
			String jwtToken = token.substring(7);
			System.out.println(""+jwtToken);
		}

		return true; // Si devuelves false, la solicitud se detendrá aquí y no se manejará más.
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// Lógica que se ejecutará después de manejar la solicitud y antes de renderizar
		// la vista
		System.out.println("Interceptor: Post-handle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// Lógica que se ejecutará después de completar la solicitud, incluso después de
		// renderizar la vista
		System.out.println("Interceptor: After completion");
	}
}
