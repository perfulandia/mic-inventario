# 📦 Microservicio de Inventario

Microservicio Spring Boot para la gestión de productos en un sistema de inventario. Ofrece operaciones CRUD completas, búsquedas flexibles y lógica de negocio robusta.

**Tecnologías**: Spring Boot, JPA/Hibernate, Lombok  

## 🔍 Características Clave  

✅ **CRUD** con validaciones  
✅ **Borrado Lógico**  
✅ **Búsquedas Flexibles**: por ID o lista de IDs  
✅ **Arquitectura Limpia**:  
- Controller-Service-Model  
- Respuestas HTTP semánticas (200, 404, 409)  
- Campos obligatorios  
- Tipado estricto  

## 🚀 Endpoints Principales  

| Método | Ruta                     | 
|--------|--------------------------|  
| GET    | `/api/v1/productos`      | 
| POST   | `/api/v1/productos`      | 
| PUT    | `/api/v1/productos/id/{id}` | 
| DELETE | `/api/v1/productos/id/{id}` | 
