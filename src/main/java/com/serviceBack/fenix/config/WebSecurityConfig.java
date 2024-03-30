package com.serviceBack.fenix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new InterceptorMidleWare())
                .addPathPatterns("/**"); // Puedes especificar patrones de URL para los cuales se aplicará el
        // interceptor
    }
}
