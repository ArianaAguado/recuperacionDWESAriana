# Streaming API

API REST para la gestión de una plataforma de streaming, desarrollada como **trabajo integrador de recuperación** de los Resultados de Aprendizaje 3, 4 y 5 de la asignatura *Desarrollo Web en Entorno Servidor*.

Permite gestionar el catálogo de series con sus episodios, los planes de suscripción, los usuarios registrados y sus suscripciones activas o históricas.

---

## Tabla de contenidos

- [Stack tecnológico](#stack-tecnológico)
- [Arquitectura](#arquitectura)
- [Modelo de datos](#modelo-de-datos)
- [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)
- [Endpoints principales](#endpoints-principales)
- [Reglas de negocio](#reglas-de-negocio)
- [Gestión de errores](#gestión-de-errores)
- [Testing](#testing)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Documentación adicional](#documentación-adicional)
- [Autora](#autora)

---

## Stack tecnológico

- **Java 21**
- **Spring Boot 4.0.6**
    - Spring Web MVC
    - Spring Data JPA
    - Spring Boot Validation
    - Spring Boot DevTools
- **Hibernate ORM 7.2**
- **H2 Database** (modo fichero en desarrollo, modo memoria en tests)
- **Lombok** para reducir boilerplate
- **JUnit 5 + Mockito** para testing
- **MockMvc** para tests de controlador
- **Maven** como gestor de dependencias

---

## Arquitectura

La aplicación sigue una arquitectura por capas clásica de Spring Boot, con responsabilidades separadas y dependencias unidireccionales.

```
Cliente (Postman / Navegador)
        │
        ▼
   CONTROLLER  ← endpoints REST, validación @Valid
        │
        ▼
    SERVICE    ← lógica de negocio, reglas del dominio
        │
        ▼
  REPOSITORY   ← acceso a datos con Spring Data JPA
        │
        ▼
       BD      ← H2 en fichero (dev) / en memoria (test)
```

Atravesando todas las capas:
- **DTOs** (Request / Response) para entrada y salida.
- **Mappers** manuales entre entidades y DTOs.
- **GlobalExceptionHandler** con `@RestControllerAdvice` para captura centralizada de errores.

---

## Modelo de datos

Cinco entidades principales con dos relaciones `@ManyToOne`:

| Entidad | Descripción |
|---|---|
| **Plan** | Planes de suscripción ofertados (Básico, Estándar, Premium...) |
| **Usuario** | Usuarios registrados en la plataforma |
| **Serie** | Series del catálogo |
| **Episodio** | Episodios pertenecientes a una serie |
| **Suscripcion** | Suscripción de un usuario a un plan, con estado y fechas |

Relaciones:

- `Serie (1) ──N→ Episodio`
- `Usuario (1) ──N→ Suscripcion (N) ──1 Plan`

Todas las relaciones son **unidireccionales** y con `FetchType.LAZY`.

---

## Cómo ejecutar el proyecto

### Requisitos

- Java 21
- Maven 3.8+ (o usar el wrapper incluido `./mvnw`)
- Git

### Pasos

1. **Clonar el repositorio:**

```bash
   git clone https://github.com/ArianaAguado/recuperacionDWESAriana.git
   cd recuperacionDWESAriana
```

2. **Ejecutar la aplicación:**

```bash
   ./mvnw spring-boot:run
```

En Windows:

```bash
   .\mvnw.cmd spring-boot:run
```

La API arranca en `http://localhost:8080`. La base de datos H2 se crea automáticamente en la carpeta `data/`.

3. **Ejecutar los tests:**

```bash
   ./mvnw test
```

4. **Acceder a la consola H2** (opcional, útil para inspeccionar la BD):

    - URL: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:file:./data/streamingdb`
    - Usuario: `sa`
    - Contraseña: *(en blanco)*

5. **Probar la API con Postman:**

   Importa el fichero `docs/streaming-api.postman_collection.json` en Postman. Incluye 27 peticiones organizadas por entidad, cubriendo CRUD, filtros y todos los casos de error.

---

## Endpoints principales

La API está versionada bajo el prefijo `/api/v1`. Se han implementado **5 controladores** con un total de **43 endpoints** (mínimo del enunciado: 32).

### Recursos disponibles

| Recurso | Ruta base | Endpoints |
|---|---|---|
| Planes | `/api/v1/planes` | 8 |
| Usuarios | `/api/v1/usuarios` | 8 |
| Series | `/api/v1/series` | 8 |
| Episodios | `/api/v1/episodios` | 8 |
| Suscripciones | `/api/v1/suscripciones` | 11 |

Cada recurso expone los 6 verbos CRUD estándar (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`) más al menos 2 endpoints de filtrado/búsqueda.

### Endpoints específicos de negocio en Suscripciones

| Verbo | Ruta | Acción |
|---|---|---|
| `POST` | `/api/v1/suscripciones` | Suscribir un usuario a un plan |
| `POST` | `/api/v1/suscripciones/{id}/cancelar` | Cancelar una suscripción activa |
| `POST` | `/api/v1/suscripciones/usuario/{usuarioId}/renovar` | Renovar tras una caducada |
| `POST` | `/api/v1/suscripciones/marcar-caducadas` | Marcar como CADUCADAS las vencidas |

---

## Reglas de negocio

Las reglas más relevantes implementadas en la capa de servicio:

1. **El nombre de un plan debe ser único.** Intentos de duplicado → `409 Conflict`.
2. **El email de un usuario debe ser único.** Intentos de duplicado → `409 Conflict`.
3. **No puede haber dos episodios con la misma serie, temporada y número.** → `409 Conflict`.
4. **Un usuario no puede tener más de una suscripción ACTIVA simultáneamente.** → `409 Conflict`.
5. **Solo se pueden cancelar suscripciones en estado ACTIVA.** Una CANCELADA o CADUCADA no se puede volver a cancelar. → `409 Conflict`.
6. **Solo se puede renovar después de tener una suscripción CADUCADA.** → `409 Conflict`.
7. **No se puede suscribir a un plan inactivo.** → `409 Conflict`.

---

## Gestión de errores

Todas las excepciones son capturadas por `GlobalExceptionHandler` con `@RestControllerAdvice` y se devuelven en un formato uniforme:

```json
{
  "timestamp": "2026-05-23T17:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Plan no encontrado con id 999",
  "path": "/api/v1/planes/999",
  "validationErrors": null
}
```

### Códigos HTTP devueltos

| Código | Significado | Cuándo |
|---|---|---|
| `200 OK` | Éxito | GET, PUT, PATCH correctos |
| `201 Created` | Recurso creado | POST correctos, con header `Location` |
| `204 No Content` | Sin cuerpo | DELETE correctos |
| `400 Bad Request` | Validación fallida | `@Valid` rechaza el body |
| `404 Not Found` | Recurso inexistente | Búsqueda por id sin resultado |
| `409 Conflict` | Conflicto de negocio | Duplicado o estado no permitido |
| `500 Internal Server Error` | Error inesperado | Fallback genérico |

---

## Testing

Se han implementado **11 tests automatizados** (mínimo del enunciado: 4 servicio + 4 controlador).

### Tests de servicio (Mockito)

| Clase | Tests |
|---|---|
| `PlanServiceTest` | 4 |
| `EpisodioServiceTest` | 1 |
| `SuscripcionServiceTest` | 2 |

### Tests de controlador (MockMvc + `@WebMvcTest`)

| Clase | Tests |
|---|---|
| `PlanControllerTest` | 1 |
| `UsuarioControllerTest` | 1 |
| `EpisodioControllerTest` | 1 |
| `SuscripcionControllerTest` | 1 |

Ejecutar todos los tests:

```bash
./mvnw test
```

Las evidencias gráficas de la ejecución se encuentran en `docs/evidencia-tests/`.

---

## Estructura del proyecto

```
streaming-api/
├── docs/
│   ├── streaming-api.postman_collection.json
│   └── evidencia-tests/
├── src/
│   ├── main/
│   │   ├── java/com/ariana/streamingapi/
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── service/         # Lógica de negocio
│   │   │   ├── repository/      # Spring Data JPA
│   │   │   ├── model/           # Entidades + enums
│   │   │   ├── dto/
│   │   │   │   ├── request/     # DTOs de entrada
│   │   │   │   └── response/    # DTOs de salida
│   │   │   ├── mapper/          # Conversión entidad ↔ DTO
│   │   │   ├── exception/       # Excepciones + handler global
│   │   │   └── StreamingApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/ariana/streamingapi/
│       │   ├── PlanServiceTest.java
│       │   ├── EpisodioServiceTest.java
│       │   ├── SuscripcionServiceTest.java
│       │   ├── PlanControllerTest.java
│       │   ├── UsuarioControllerTest.java
│       │   ├── EpisodioControllerTest.java
│       │   └── SuscripcionControllerTest.java
│       └── resources/
│           └── application.properties  # Config H2 en memoria
├── pom.xml
└── README.md
```

## Autora

**Ariana Aguado Guijarro**
Recuperación – Mayo 2026
Asignatura: *Desarrollo Web en Entorno Servidor*