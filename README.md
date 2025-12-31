# 🚀 Kimbo – Transaction Orchestrator Platform

![Java](https://img.shields.io/badge/Java-1.8-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen)
![Angular](https://img.shields.io/badge/Angular-Frontend-red)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![Swagger](https://img.shields.io/badge/API-Swagger-green)
![JWT](https://img.shields.io/badge/Security-JWT-yellow)
![Linux](https://img.shields.io/badge/Infra-Linux%20VPS-black)

Kimbo is a **transaction process orchestration platform** designed to model and execute complete business workflows in a **database-driven and configurable way**, without requiring code changes.

---

## 🧠 Overview

Kimbo is an enterprise solution composed of **backend, frontend, and database**, designed to decouple business process logic from source code.

It allows defining **forms, workflows, states, responsibilities, and validations** directly in the database, enabling flexible and dynamic business process execution.

The system was implemented for a **real-world shipment and import management use case**, covering the full operational lifecycle.

---

## 🧩 Solution Components

### ⚙ Backend – REST API
📦 **fenix_rest**  
https://github.com/arrAugusto/fenix_rest

- Java 1.8 + Spring Boot  
- Transaction process orchestration engine  
- State management and validations  
- JWT-based authentication  
- API documentation with Swagger  

---

### 🎨 Frontend – Web Application
📦 **kimbo**  
https://github.com/arrAugusto/kimbo

- Angular  
- Dynamic form rendering  
- Stage-based workflows (creation, tracking, closure)  
- Role- and state-based UI  
- REST API integration  

---

### 🗄 Database
📦 **kimbo_database**  
https://github.com/arrAugusto/kimbo_database

- MySQL  
- Normalized relational data model  
- Workflow configuration and traceability  
- Document control and operational metrics  

---

## 🔄 Functional Workflow

1. **Creation** – Initial process registration  
2. **Tracking** – Operational updates during execution  
3. **Closure** – Document control and final metrics  

---

## 🏗 Architecture

- Frontend: Angular  
- Backend: Spring Boot (REST)  
- Database: MySQL  
- Infrastructure: Linux VPS, Apache, Tomcat  

The architecture is **decoupled, scalable, and process-oriented**, suitable for transactional and enterprise environments.

---

## ⚠ Disclaimer

This repository represents a **real production implementation**.  
Public repositories do **not** contain sensitive or production data.

---

## 👤 Author

**Augusto Rufino Gómez Concúan**  
Architecture · Backend · Frontend
