# biblioteca-api

API REST para gestión de una biblioteca, desarrollada con **Spring Boot 3.2**, **Spring Data JPA**, **DTOs**, manejo global de errores y documentación **Swagger UI**.

Laboratorio Unidad 5 — Post-Contenido 1 y 2  
Patrones de Diseño de Software · Ingeniería de Sistemas · UDES · 2026

---

## Arquitectura

```
HTTP Request
     │
     ▼
┌──────────────────────────────────────────┐
│  Controller v1 /api/libros               │
│  Controller v2 /api/v2/libros  (DTOs)    │
└────────────────────┬─────────────────────┘
                     │ delega
                     ▼
┌──────────────────────────────────────────┐
│  LibroMapper                             │
│  Convierte entre Entidad <-> DTO         │
└────────────────────┬─────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────┐
│  LibroService — logica de negocio        │
└────────────────────┬─────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────┐
│  LibroRepository — acceso a datos JPA    │
└────────────────────┬─────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────┐
│  Libro (Entity) → tabla "libros" en H2   │
└──────────────────────────────────────────┘

Excepciones capturadas globalmente por:
  GlobalExceptionHandler (@RestControllerAdvice)
```

---

## Estructura del proyecto

```
biblioteca-api/
├── pom.xml
└── src/main/java/com/universidad/patrones/
    ├── BibliotecaApiApplication.java
    ├── model/
    │   └── Libro.java                    ← Entidad JPA
    ├── repository/
    │   └── LibroRepository.java          ← Patron Repository
    ├── service/
    │   └── LibroService.java             ← Logica de negocio
    ├── dto/
    │   ├── LibroRequestDTO.java          ← Datos de entrada (cliente → API)
    │   └── LibroResponseDTO.java         ← Datos de salida (API → cliente)
    ├── mapper/
    │   └── LibroMapper.java              ← Convierte Entidad <-> DTO
    ├── exception/
    │   └── GlobalExceptionHandler.java   ← Manejo global de errores
    └── controller/
        ├── LibroController.java          ← v1: /api/libros
        └── LibroControllerV2.java        ← v2: /api/v2/libros (con DTOs)
```

---

## Requisitos

| Herramienta  | Version minima |
|--------------|----------------|
| Java JDK     | 17             |
| Apache Maven | 3.8+           |

---

## Dependencias principales

| Dependencia | Proposito |
|-------------|-----------|
| `spring-boot-starter-web` | Controladores REST |
| `spring-boot-starter-data-jpa` | Patron Repository con Hibernate |
| `spring-boot-starter-validation` | Validacion con @Valid, @NotBlank |
| `h2` | Base de datos embebida en memoria |
| `springdoc-openapi-starter-webmvc-ui` | Documentacion Swagger UI |

---

## Como ejecutar

```bash
git clone https://github.com/tu-usuario/apellido-post2-u5.git
cd apellido-post2-u5
mvn spring-boot:run
```

La aplicacion inicia en **http://localhost:8080**

---

## Endpoints disponibles

### v1 — `/api/libros`

| Metodo | Ruta | Descripcion | HTTP |
|--------|------|-------------|------|
| GET | `/api/libros` | Listar todos | 200 |
| GET | `/api/libros/{id}` | Obtener por ID | 200 / 404 |
| POST | `/api/libros` | Crear libro | 201 |
| PUT | `/api/libros/{id}` | Actualizar | 200 / 404 |
| DELETE | `/api/libros/{id}` | Eliminar | 204 / 404 |
| GET | `/api/libros/buscar?q=` | Buscar por titulo | 200 |

### v2 — `/api/v2/libros` (con DTOs y Swagger)

| Metodo | Ruta | Descripcion | HTTP |
|--------|------|-------------|------|
| GET | `/api/v2/libros` | Listar todos | 200 |
| GET | `/api/v2/libros/{id}` | Obtener por ID | 200 / 404 |
| POST | `/api/v2/libros` | Crear libro | 201 / 400 |
| DELETE | `/api/v2/libros/{id}` | Eliminar | 204 / 404 |

### Otros

| URL | Descripcion |
|-----|-------------|
| `http://localhost:8080/swagger-ui.html` | Documentacion interactiva Swagger |
| `http://localhost:8080/h2-console` | Consola H2 (JDBC URL: `jdbc:h2:mem:biblioteca_db`, user: `sa`) |

---

## Manejo de errores

| Excepcion | Codigo HTTP | Ejemplo de respuesta |
|-----------|-------------|----------------------|
| Libro no encontrado | 404 | `{"error": "Libro no encontrado con id: 999"}` |
| ISBN duplicado | 400 | `{"error": "Ya existe un libro con ISBN: 978-..."}` |
| Datos invalidos (@Valid) | 400 | `{"errores": ["titulo: El titulo es obligatorio"]}` |

---

## Capturas de pantalla




## Autor

**Jair Sanjuan**  
Ingenieria de Sistemas — Universidad de Santander (UDES)  
Patrones de Diseno de Software · 2026
