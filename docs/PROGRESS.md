# 📓 Bitácora de Progreso e Hitos - G9-LATAM-TEAM-54

## 🧪 REPORTE DE AVANCES 20-07-2026

### 1. Resumen de Cambios Realizados
* **Definición de dataset:** Luego de analizar el proyecto, se consultaron distintos repositorios de datasets. Se seleccionó el dataset `household_energy_consumption`, que presenta el consumo energético de varios hogares durante un mes y contempla variables como consumo total de energía, número de integrantes del hogar y consumo en horas punta, entre otras.

## 🧪 REPORTE DE AVANCES 27-07-2026

### 1. Resumen de Cambios Realizados
* **Limpieza de datos y exploración inicial:** se creó un notebook y, luego de la importación de librerías y la carga del dataset, se procedió con la limpieza de los datos para detectar valores faltantes, duplicados y errores y estandarizar formatos como fechas y unidades de medida. A continuación, se realizó la exploración inicial, para identificar tendencias generales, como patrones de consumo por día de la semana y uso en horas punta, y se generaron visualizaciones preliminares.
* **Procesamiento y transformación de variables:** se crearon nuevas variables, como indicadores de eficiencia, consumo per cápita y variaciones diarias, y se seleccionaron variables relevantes para el modelo supervisado.
* **Entrenamiento del modelo supervisado:** se dividió el dataset en un grupo de prueba y otro de entrenamiento mediante train-test split y se entrenó el modelo Random Forest para clasificar hogares en tres niveles de eficiencia.
* **Evaluación del modelo:** se analizaron métricas de desempeño (precision, recall, f1-score,matriz de confusión) para validar el modelo y se creó un sistema de recomendaciones automatizadas basadas en reglas y predicciones del modelo.
* **Serialización del modelo:** se generan los archivos `modelo_eficiencia.pkl` y `scaler_eficiencia.pkl` para reutilizar el modelo sin necesidad de reentrenarlo, facilitando así su integración en aplicaciones.



---

## ⚙️ 2. Backend & Arquitectura (Spring Boot)

### Card #05: IMPLEMENTACIÓN DE DTOs (Completada) ✅

* **Objetivo:** Crear las clases que representan las solicitudes y respuestas de la API, asegurando la integridad y el tipado desde la entrada hasta la salida.

* **Avances técnicos — Request DTOs:**

| Clase | Campo | Tipo | Validación |
| :--- | :--- | :--- | :--- |
| `AnalisisRequest` | `consumo_kwh` | `Double` | `@NotNull`, `@Min(0)` |
| `AnalisisRequest` | `householdSize` | `Integer` | `@NotNull`, `@Min(1)` |
| `AnalisisRequest` | `avgTemperatureC` | `Double` | `@NotNull` |
| `AnalisisRequest` | `hasAc` | `Boolean` | `@NotNull` |
| `AnalisisRequest` | `peakHoursUsageKwh` | `Double` | `@NotNull`, `@Min(0)` |
| `AnalisisRequest` | `cantidadEquipos` | `Integer` | `@NotNull`, `@Min(0)` |

* **Avances técnicos — Response DTOs:**

| Clase | Campos principales | Descripción |
| :--- | :--- | :--- |
| `AnalisisResponse` | `id`, `consumoActual`, `costoEstimado`, `categoria`, `probabilidad`, `recomendaciones`, `fechaCreacion` | Respuesta base del análisis energético |
| `FinalAnalisisResponse` | Todos los de `AnalisisResponse` + `desgloseEstancias`, `householdSize`, `cantidadEquipos` | Respuesta completa con desglose por estancia |
| `DashboardResponse` | `consumoTotal`, `costoTotal`, `categoria`, `desgloseEstancias`, `recomendaciones` | Resumen del dashboard del usuario |
| `DispositivoResponse` | `id`, `alias`, `nombreEquipo`, `nombreVariante`, `horasUsoDiarias`, `consumoMensualKwh`, `nombreEstancia` | Representación de dispositivo del usuario |
| `EstadisticasResponse` | `totalAnalisis`, `consumoPromedioKwh`, `costoPromedioMensual`, `consumoTotalKwh`, `costoTotalMensual` | Métricas agregadas del historial |
| `ErrorResponse` | `timestamp`, `status`, `error`, `message`, `path` | Estructura estándar de errores de la API |

* **Enum de Categorías:**
    * Se definió `CategoriaEnergetica` como `enum` con tres valores: `EFICIENTE`, `MODERADO`, `INEFICIENTE`. La entidad lo persiste en base de datos como `String` mediante `@Enumerated(EnumType.STRING)`.

---

### Card #06: LÓGICA DE NEGOCIO Y SERVICIOS (Completada) ✅

* **Objetivo:** Estructurar la arquitectura de servicios desacoplados para mantener el código limpio y mantenible.

* **`PredictionService` — Motor de Inferencia ONNX:**
    * Carga el modelo `EnergiAI_model.onnx` desde el classpath al iniciar la aplicación mediante `@PostConstruct`.
    * Inicializa `OrtEnvironment` y `OrtSession` de ONNX Runtime para la inferencia nativa en la JVM, sin microservicio Python externo.
    * Extrae el resultado del modelo mediante los outputs `output_label` (categoría) y `output_probability` (confianza).
    * Implementa **modo simulación**: si el modelo no está disponible en el classpath, aplica umbrales de consumo (`< 10 kWh` → EFICIENTE, `> 18 kWh` → INEFICIENTE) como fallback.
    * Detecta anomalías: consumo negativo fuerza la categoría `EFICIENTE` con probabilidad `1.0`.
    * Libera los recursos de ONNX correctamente en `@PreDestroy`.
    * Expone el resultado mediante el record interno `PrediccionResultado(CategoriaEnergetica, double)`.

* **`CostService` — Motor Financiero:**
    * Calcula el costo mensual estimado aplicando una tarifa fija de **$ 0,75 por kWh**.
    * Fórmula: `costoEstimado = consumoKwh × 0.75`

* **`RecommendationsService` — Motor de Reglas:**
    * Genera recomendaciones dinámicas evaluando la `CategoriaEnergetica` resultante de la predicción.
    * Incluye una alerta adicional si el consumo diario equivalente supera los **18 kWh**.

    | Categoría | Recomendación generada |
    | :--- | :--- |
    | `INEFICIENTE` | Apagar equipos pesados en horas pico |
    | `MODERADO` | Consumo equilibrado, mantener hábitos |
    | `EFICIENTE` | Consumo dentro del rango óptimo |

* **`AnalisisService` — Orquestador Principal:**
    * Centraliza el flujo completo: recupera los dispositivos del usuario desde la BD, agrega el consumo, invoca a `PredictionService`, luego a `CostService` y `RecommendationsService`.
    * Persiste el resultado en `AnalisisEntity` con referencia al usuario autenticado (`@ManyToOne` → `User`).
    * Expone métodos adicionales: `obtenerHistorialPorEmail`, `obtenerEstadisticasPorEmail` (promedios y totales) y `eliminarAnalisis`.

---

### Card #07: CONTROLADOR REST (ENDPOINTS) (Completada) ✅

* **Objetivo:** Exponer los endpoints oficiales del sistema manteniendo un controlador delgado (*thin controller*), delegando toda la lógica a la capa de servicios.

* **`AnalisisController` — Endpoints implementados:**

| Método | Endpoint | Auth | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/analisis-energetico` | 🔒 JWT | Ejecuta el análisis completo del usuario autenticado y persiste el resultado |
| `POST` | `/api/analisis/calcular-dispositivo` | 🔒 JWT | Calcula el consumo mensual estimado de un dispositivo específico |
| `GET` | `/api/analisis/{id}` | 🔒 JWT | Consulta un análisis por ID (solo si pertenece al usuario autenticado) |
| `GET` | `/api/historial` | 🔒 JWT | Retorna el historial completo de análisis del usuario autenticado |
| `GET` | `/api/estadisticas` | 🔒 JWT | Retorna las métricas agregadas del usuario (promedio, total kWh, costo) |
| `DELETE` | `/api/analisis/{id}` | 🔒 JWT | Elimina un análisis del historial |

* **Detalles de implementación:**
    * El controlador utiliza `Principal` de Spring Security para obtener el email del usuario autenticado en cada petición, sin exponer el ID de usuario en la URL.
    * La consulta `GET /api/analisis/{id}` retorna `403 FORBIDDEN` si el análisis no pertenece al usuario que realiza la petición.
    * Decorado con `@RestController` y `@RequestMapping("/api")` para el prefijo global de la API.

=== "POST /api/analisis-energetico"

    ```json
    // Respuesta 200 OK
    {
      "id": 1,
      "categoria": "MODERADO",
      "probabilidad": 0.82,
      "consumoActual": 250.5,
      "costoEstimado": 187.875,
      "recomendaciones": ["El consumo se encuentra en un rango moderado y equilibrado."],
      "desgloseEstancias": [...],
      "householdSize": 3,
      "cantidadEquipos": 5
    }
    ```

=== "GET /api/estadisticas"

    ```json
    // Respuesta 200 OK
    {
      "totalAnalisis": 4,
      "consumoPromedioKwh": 230.75,
      "costoPromedioMensual": 173.06,
      "consumoTotalKwh": 923.0,
      "costoTotalMensual": 692.25
    }
    ```

---

### Card #08: MANEJO GLOBAL DE ERRORES (Completada) ✅

* **Objetivo:** Centralizar la captura de excepciones con `@RestControllerAdvice` para garantizar respuestas JSON estandarizadas y códigos HTTP correctos en todos los escenarios de error.

* **`GlobalExceptionHandler` — Manejadores implementados:**

| Excepción capturada | HTTP Status | Descripción |
| :--- | :--- | :--- |
| `MethodArgumentNotValidException` | `400 Bad Request` | Campos del request que no pasan las validaciones de Jakarta (campo a campo) |
| `ModelInferenceException` | `500 Internal Server Error` | Fallo en la carga o ejecución del modelo ONNX |
| `Exception` (genérico) | `500 Internal Server Error` | Cualquier error no controlado del sistema |

* **Excepción personalizada `ModelInferenceException`:**
    * Extiende `RuntimeException`, lanzada desde `PredictionService` ante fallos críticos de ONNX Runtime.
    * Capturada específicamente por `GlobalExceptionHandler` para retornar un mensaje descriptivo del error de inferencia.

* **Estructura estándar de respuesta de error:**

```json
{
  "timestamp": "2026-08-23T17:00:00",
  "status": 400,
  "error": "Datos de entrada inválidos",
  "detalles": {
    "consumo_kwh": "El consumo en kWh es obligatorio",
    "householdSize": "El tamaño del hogar debe ser al menos 1"
  }
}
```

!!! note "Validaciones de campo"
    Los errores `400` incluyen el mapa `detalles` con cada campo inválido y su mensaje de validación, permitiendo al frontend identificar exactamente qué campos corregir.

---

## 🗃️ REPORTE DE AVANCES 07-08-2026

### 1. Resumen de Cambios Realizados
* **Persistencia y Base de Datos:** Se configuró PostgreSQL con volúmenes persistentes en Docker Compose, garantizando que los datos no se pierdan al apagar los contenedores.
* **Actualización del Backend (Spring Boot):** Se adaptaron las entidades y servicios: categoría del perfil energético (Eficiente, Moderado, Ineficiente), probabilidad y costo estimado basado en la tarifa de referencia de $0,75 por kWh.
* **Endpoints Operativos:**
    * `POST /api/analisis-energetico`: Procesa los datos de consumo, guarda el registro en la base de datos y devuelve el JSON requerido con su ID.
    * `GET /api/analisis/{id}`: Permite consultar el historial de un análisis específico desde la base de datos.

---

### 2. Documentación y Puerto de Pruebas (Scalar)
* El proyecto incluye integración con Scalar para la documentación interactiva de la API.
* Una vez que la aplicación está corriendo, puedes acceder a la interfaz gráfica de Scalar en tu navegador en el siguiente puerto y ruta: `http://localhost:8082/scalar.html` (o según el puerto configurado en tu docker-compose).

---

## 🗃️ REPORTE DE AVANCES 09-08-2026

### 1. Resumen de Cambios Realizados
* **Actualización de Rama y Base de Datos:** Se actualizó la rama 8 y se integró Adminer como interfaz gráfica ligera para la base de datos PostgreSQL mediante Docker.

---

### 2. Documentación y Puerto de Pruebas (Adminer / Scalar)
* **Interfaz de Base de Datos (Adminer):** 
  Disponible en `http://localhost:8083/`. Configurar los credenciales requeridos (contraseña por defecto: `tu_contrasena`, modificable en el archivo `application.yaml` del backend). Rellenar el resto de campos como en las imágenes adjuntas.
* **Documentación de la API (Scalar):** 
  Disponible en `http://localhost:8082/scalar.html` para la ejecución de métodos POST y pruebas de endpoints.

---

### 3. Estructura de Datos y Payloads (`POST /api/analisis-energetico`)

* **Parámetros requeridos en la Petición:**
  
| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `consumo_kwh` | `Double` | Cantidad de energía consumida en kWh (Ej: 250.5). |
| `tipo_inmueble` | `String` | Clasificación del inmueble ("RESIDENCIAL", "COMERCIAL", etc.). |
| `uso_horario_pico` | `Boolean` | Indicador de consumo en horarios pico (`true` / `false`). |
| `cantidad_equipos` | `Integer` | Número de artefactos eléctricos asociados (Ej: 3). |
| `horas_alto_consumo` | `Integer` | Horas estimadas de uso intensivo (Ej: 4). |

* **Ejemplo de Uso:**

=== "Petición (Request)"

    ```json
    {
      "consumo_kwh": 250.5,
      "tipo_inmueble": "RESIDENCIAL",
      "uso_horario_pico": true,
      "cantidad_equipos": 3,
      "horas_alto_consumo": 4
    }
    ```

=== "Respuesta Exitosa (200 OK)"

    ```json
    {
      "recomendaciones": [
        "El consumo actual es elevado. Considere apagar equipos..."
      ],
      "categoria": "Moderado",
      "consumoActual": 250.5,
      "probabilidad": 0.82,
      "costoEstimado": 187.875,
      "id": 1
    }
    ```

---

### 4. Pruebas de Integración y Validación (QA)
* **Happy Path (POST):** Validado exitosamente a través de la interfaz de Scalar. La petición procesa los datos, calcula el perfil energético y persiste la información asignando un ID único en la base de datos PostgreSQL.
* **Consulta de Historial (GET):** Validada la recuperación del registro persistido mediante el endpoint `/api/analisis/{id}` retornando código HTTP `200 OK` y la marca de tiempo correspondiente.

---

### 5. Configuración y Variables de Entorno
* **Puerto de la Aplicación (Spring Boot):** `8082`
* **Puerto de la Base de Datos (PostgreSQL):** `5432`
* **Motor de Virtualización:** `WSL 2 / Docker Engine`
* **Nombre del Contenedor de BD:** `energiai-postgres`
* **Nombre del Contenedor de la API:** `api-energi-ai`

---

### 6. Ejemplos de Peticiones (cURL)

=== "Petición POST"

    ```bash
    curl -X POST http://localhost:8082/api/analisis-energetico \
      -H "Content-Type: application/json" \
      -d '{"consumo_kwh": 250.5, "tipo_inmueble": "RESIDENCIAL", "uso_horario_pico": true, "cantidad_equipos": 3, "horas_alto_consumo": 4}'
    ```

=== "Petición GET"

    ```bash
    curl -X GET http://localhost:8082/api/analisis/1
    ```

---

## 🔐 REPORTE DE AVANCES 11-08-2026 al 14-08-2026

### 1. Resumen de Cambios Realizados
* **Implementación de Seguridad y Autenticación JWT:** Se integró Spring Security con autenticación basada en JSON Web Tokens (JWT) mediante la librería JJWT (`0.11.5`). La arquitectura de seguridad queda conformada por los siguientes componentes:
    * `SecurityConfig`: Configuración central de la cadena de filtros HTTP, definiendo rutas públicas y protegidas.
    * `JwtTokenProvider`: Generación, firma y validación de tokens JWT con clave secreta configurable vía variable de entorno (`JWT_SECRET`).
    * `JwtAuthenticationFilter`: Filtro de solicitudes que intercepta y valida el token `Bearer` en cada petición entrante.
* **Endpoints de Autenticación:** Se implementó `AuthController` exponiendo los siguientes endpoints públicos:
    * `POST /api/auth/register`: Registro de nuevos usuarios con contraseña encriptada (`BCryptPasswordEncoder`).
    * `POST /api/auth/login`: Autenticación de credenciales y emisión del token JWT de sesión.
* **Gestión de Usuarios:** Se creó `UserController` y `UserService` para la administración del perfil de usuario autenticado.
* **Modelo de Datos de Usuario:** Definición de la entidad `User` con campos de username, contraseña hasheada y rol, persistida en PostgreSQL.

---

### 2. Configuración de CORS
* Se implementó `CorsConfig` para permitir el consumo de la API desde el frontend Vue 3, habilitando los orígenes y métodos HTTP necesarios para el correcto funcionamiento de la SPA.

---

## 🖥️ REPORTE DE AVANCES 15-08-2026 al 17-08-2026

### 1. Resumen de Cambios Realizados
* **Inicio del Frontend (Vue 3 + Vite):** Se inicializó el proyecto frontend con el stack definido: **Vue 3**, **Vite**, **Tailwind CSS** y **DaisyUI** como sistema de componentes.
* **Estructura de Componentes:** Se organizó la arquitectura de componentes del frontend en módulos funcionales:
    * `auth/`: Pantallas de Login y Registro de usuario.
    * `dashboard/`: Panel principal con resumen del perfil energético del usuario.
    * `layout/`: Componentes estructurales como Navbar, Sidebar y Footer.
    * `common/`: Componentes reutilizables compartidos entre vistas.
* **Capa de Integración API:** Se configuró la carpeta `api/` con Axios para gestionar las peticiones HTTP al backend, incluyendo el interceptor de autorización que adjunta automáticamente el token JWT en los headers.
* **Dockerización del Frontend:** Se creó el `Dockerfile` del frontend con una imagen Nginx para servir el build de producción, y se registró el servicio `frontend` en el `docker-compose.yaml` expuesto en el puerto `8084`.

---

### 2. Configuración del Frontend

| Servicio | Puerto | Descripción |
| :--- | :--- | :--- |
| Frontend (Vite dev) | `5173` | Servidor de desarrollo local |
| Frontend (Docker/Nginx) | `8084` | Contenedor de producción |

---

## 📊 REPORTE DE AVANCES 18-08-2026 al 21-08-2026

### 1. Resumen de Cambios Realizados
* **Módulo de Dashboard y Visualizaciones:** Se implementó el dashboard principal con gráficos interactivos de consumo energético utilizando **Chart.js** y **vue-chartjs**. El componente consume el endpoint `GET /api/dashboard` provisto por `DashboardController` y `DashboardService`.
* **Gestión de Dispositivos y Estancias:** Se desarrollaron los módulos `devices/` y `reports/`, integrados con los siguientes endpoints del backend:
    * `DispositivoController` (`/api/dispositivos`): Gestión del catálogo y selección de dispositivos eléctricos del usuario.
    * `EquipoController` (`/api/equipos`): Administración de variantes de equipos y su consumo asociado.
    * `EstanciaController` (`/api/estancias`): Organización del consumo por habitaciones o zonas del inmueble.
    * `CatalogoController` (`/api/catalogo`): Exposición del catálogo público de equipos disponibles.
* **Módulo de Tendencias:** Se implementó el componente `trends/` para visualizar la evolución temporal del consumo energético del usuario mediante gráficos de línea.
* **DTOs Extendidos:** Se crearon los DTOs de respuesta adicionales requeridos por los nuevos módulos: `DashboardResponse`, `DispositivoResponse`, `EquipoCatalogoResponse`, `EquipoVarianteResponse`, `EstanciaDesgloseResponse`, `EstadisticasResponse` y `FinalAnalisisResponse`.
* **Inicialización de Datos de Catálogo:** Se implementó `DataInitializer` con `@PostConstruct` para poblar automáticamente las tablas de catálogo de equipos y opciones de temperatura al inicio de la aplicación.

---

### 2. Nuevos Endpoints Operativos

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/dashboard` | Resumen del perfil energético del usuario autenticado |
| `GET` | `/api/dispositivos` | Lista los dispositivos registrados del usuario |
| `POST` | `/api/dispositivos/seleccion` | Registra la selección de dispositivos del usuario |
| `GET` | `/api/equipos/catalogo` | Catálogo completo de equipos disponibles |
| `GET` | `/api/estancias` | Desglose de consumo por estancia del inmueble |
| `GET` | `/api/catalogo/temperatura` | Opciones de temperatura disponibles |

---

### 3. Configuración Inicial del Usuario

* Se implementó el endpoint `POST /api/usuarios/configuracion-inicial` mediante `ConfiguracionInicialRequest`, que permite a los nuevos usuarios registrar su configuración de inmueble, dispositivos y estancias en un único paso tras el primer inicio de sesión.

---

## 🚀 REPORTE DE AVANCES 22-08-2026 al 23-08-2026

### 1. Resumen de Cambios Realizados
* **Tareas Programadas (Scheduler):** Se implementó `ReporteScheduler` utilizando `@Scheduled` de Spring para la generación automática de reportes periódicos de consumo energético por usuario.
* **Manejo Global de Errores (Completado):** Se finalizó e implementó `GlobalExceptionHandler` con `@RestControllerAdvice`, centralizando la captura de excepciones y garantizando respuestas JSON estandarizadas con los códigos HTTP correctos. Se incluyó tratamiento específico para:
    * Errores de validación de campos (`MethodArgumentNotValidException`).
    * Excepción personalizada `ModelInferenceException` para fallos en la inferencia del modelo ONNX.
    * Errores de autenticación y autorización (401, 403).
    * Recursos no encontrados (404).
* **Containerización Completa:** Se verificó y validó el `docker-compose.yaml` con los cuatro servicios del sistema corriendo de forma orquestada y estable:

| Contenedor | Imagen | Puerto | Descripción |
| :--- | :--- | :--- | :--- |
| `energiai-postgres` | `postgres:15-alpine` | `5432` | Base de datos relacional |
| `api-energi-ai` | Build local `./backend` | `8082` | API REST Spring Boot |
| `energiai-frontend` | Build local `./frontend` | `8084` | SPA Vue 3 (Nginx) |
| `energiai-adminer` | `adminer` | `8083` | Interfaz de administración de BD |

* **Documentación del Proyecto:** Se actualizó la documentación técnica en MkDocs (`docs/`) con los apartados de API, Arquitectura y Progreso, y se desplegó en Vercel para acceso del equipo.

---

### 2. Estado Final del Sistema

!!! success "Sistema Operativo"
    Todos los servicios del stack han sido validados y se encuentran operativos. El flujo completo de autenticación → análisis energético → persistencia → dashboard está funcional end-to-end.

| Componente | Estado |
| :--- | :--- |
| API REST (Spring Boot) | ✅ Operativo |
| Base de Datos (PostgreSQL) | ✅ Operativo |
| Autenticación JWT | ✅ Operativo |
| Inferencia ONNX | ✅ Operativo |
| Frontend (Vue 3) | ✅ Operativo |
| Documentación (MkDocs/Vercel) | ✅ Desplegado |
| Containerización (Docker Compose) | ✅ Validado |

---

### 3. Servicios Disponibles

| Servicio | URL | Descripción |
| :--- | :--- | :--- |
| Frontend | `http://localhost:8084` | Interfaz de usuario Vue 3 |
| API REST | `http://localhost:8082` | Endpoints de la API |
| Scalar (Docs) | `http://localhost:8082/scalar.html` | Documentación interactiva |
| Adminer (BD) | `http://localhost:8083` | Interfaz de administración PostgreSQL |
