package com.serviceBack.fenix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.serviceBack.fenix")
public class FenixApplication {
    public static void main(String[] args) {
        SpringApplication.run(FenixApplication.class, args);
    }
}