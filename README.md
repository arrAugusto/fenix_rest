# 🚀 Kimbo – Transaction Orchestrator Platform

![Java](https://img.shields.io/badge/Java-1.8-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen)
![Angular](https://img.shields.io/badge/Angular-Frontend-red)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![Swagger](https://img.shields.io/badge/API-Swagger-green)
![JWT](https://img.shields.io/badge/Security-JWT-yellow)
![Linux](https://img.shields.io/badge/Infra-Linux%20VPS-black)

Plataforma **orquestadora de procesos transaccionales** diseñada para modelar y ejecutar flujos empresariales completos de forma **configurable desde base de datos**, sin necesidad de cambios en código.

---

## 🧠 Descripción general

Kimbo es una solución empresarial compuesta por **backend, frontend y base de datos**, que desacopla la lógica del proceso del código fuente.  
Permite definir **formularios, flujos, estados, responsables y validaciones** directamente desde la base de datos.

El sistema fue aplicado a un **caso real de gestión de embarques e importaciones**, cubriendo todo el ciclo operativo.

---

## 🧩 Proyectos que componen la solución

### ⚙ Backend – API REST
📦 **fenix_rest**  
https://github.com/arrAugusto/fenix_rest

- Java 1.8 + Spring Boot
- Orquestación de procesos transaccionales
- Validaciones y control de estados
- Autenticación con JWT
- Documentación con Swagger

---

### 🎨 Frontend – Aplicación Web
📦 **kimbo**  
https://github.com/arrAugusto/kimbo

- Angular
- Formularios dinámicos
- Flujo por etapas (registro, seguimiento, cierre)
- UI por roles y estados
- Consumo de APIs REST

---

### 🗄 Base de Datos
📦 **kimbo_database**  
https://github.com/arrAugusto/kimbo_database

- MySQL
- Modelo relacional normalizado
- Catálogos, flujos y trazabilidad
- Control documental y métricas

---

## 🔄 Flujo funcional implementado

1. **Registro** – Creación inicial del proceso  
2. **Seguimiento** – Actualización operativa en tránsito  
3. **Cierre** – Control documental y métricas finales  

---

## 🏗 Arquitectura

- Frontend: Angular
- Backend: Spring Boot (REST)
- Base de datos: MySQL
- Infraestructura: Linux VPS, Apache, Tomcat

Arquitectura desacoplada, escalable y orientada a procesos.

---

## ⚠ Nota

Este proyecto representa una **implementación real**.  
Los repositorios públicos no contienen información sensible ni datos productivos.

---

## 👤 Autor

**Augusto Rufino Gómez Concúan**  
Arquitectura · Backend · Frontend

