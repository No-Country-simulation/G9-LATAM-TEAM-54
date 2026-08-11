# 📊 Bitácora de Progreso e Hitos - G9-LATAM-TEAM-54

Este documento recopila el avance cronológico y el estado de las tareas del proyecto, categorizadas por áreas de desarrollo.

---

## 🤖 1. Data Science & Machine Learning (En curso) 🛠

*(Datos por agregar).*

---

## ☕ 2. Backend & Arquitectura (Spring Boot)

### Card #05: IMPLEMENTACIÓN DE DTOs (Completada) ✔
* **Objetivo:** Crear las clases que representan las solicitudes y respuestas de la API, asegurando la integridad desde la entrada.
* **Avances técnicos:**
    * Creación de `AnalisisRequest` incorporando las validaciones de Jakarta (`@NotNull`, `@Min`, `@NotBlank`) para los campos clave.
    * Definición de `AnalisisResponse` estructurando los campos de salida (categoría, probabilidad, recomendaciones y costo estimado mensual).
    * Implementación de `ErrorResponse` para estandarizar el manejo de errores.

### Card #06: LÓGICA DE NEGOCIO Y SERVICIOS (Completada) ✔
* **Objetivo:** Estructurar la arquitectura de servicios desacoplados para mantener el código limpio y mantenible.
* **Avances técnicos:**
    * Configuración de `PredictionService` para la ejecución del modelo ONNX de forma nativa en Spring Boot (`@PostConstruct` / `@PreDestroy`).
    * Desarrollo de `CostService` para el cálculo financiero.
    * Creación del motor de reglas en `RecommendationsService`.
    * Integración de la orquestación central en `AnalisisService`.

### Card #07: CONTROLADOR REST (ENDPOINTS) (En curso) 🛠
* **Objetivo:** Exponer los endpoints oficiales del sistema manteniendo un controlador delgado (*thin controller*).
* **Avances técnicos:**

### Card #08: MANEJO GLOBAL DE ERRORES (Por iniciar) ⌛
* **Objetivo:** Centralizar la captura de excepciones para garantizar respuestas JSON limpias y con los códigos HTTP correctos.
* **Avances técnicos:**
  * **REPORTE DE AVANCES 09-08-2026**
  
**1. RESUMEN DE CAMBIOS REALIZADOS**

---

* **Actualización de Rama y Base de Datos:**

  - Se actualizó la rama 8 y se integró Adminer como interfaz gráfica
    ligera para la base de datos PostgreSQL mediante Docker.

**2. DOCUMENTACIÓN Y PUERTO DE PRUEBAS (ADMINER / SCALAR)**

---

* **Interfaz de Base de Datos (Adminer):**

  - Disponible en http://localhost:8083/.
    Configurar los credenciales requeridos (contraseña por defecto:
    tu_contrasena, modificable en el archivo application.yaml del backend).
    Rellenar el resto de campos como en las imagenes adjuntas.

* **Documentación de la API (Scalar):**

  - Disponible en http://localhost:8082/scalar.html para la ejecución
    de métodos POST y pruebas de endpoints.

**3. ESTRUCTURA DE DATOS Y PAYLOADS (POST /api/analisis-energetico)**

---

**Campos requeridos en el JSON:**
- consumo_kwh (Double): Cantidad de energía consumida en kWh (Ej: 250.5).
- tipo_inmueble (String): Clasificación del inmueble ("RESIDENCIAL", "COMERCIAL", etc.).
- uso_horario_pico (Boolean): Indicador de consumo en horarios pico (true/false).
- cantidad_equipos (Integer): Número de artefactos eléctricos asociados (Ej: 3).
- horas_alto_consumo (Integer): Horas estimadas de uso intensivo (Ej: 4).

**Ejemplo de Respuesta Exitosa (200 OK):**
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

**4. PRUEBAS DE INTEGRACIÓN Y VALIDACIÓN (QA)**

---

- Happy Path (POST): Validado exitosamente a través de la interfaz de Scalar. La petición procesa los datos, calcula el perfil energético y persiste la información asignando un ID único en la base de datos PostgreSQL.
- Consulta de Historial (GET): Validada la recuperación del registro persistido mediante el endpoint /api/analisis/{id} retornando código HTTP 200 OK y la marca de tiempo correspondiente.

**5. CONFIGURACIÓN Y VARIABLES DE ENTORNO**

---

- Puerto de la Aplicación (Spring Boot): 8082
- Puerto de la Base de Datos (PostgreSQL): 5432
- Motor de Virtualización: WSL 2 / Docker Engine
- Nombre del Contenedor de BD: energiai-postgres
- Nombre del Contenedor de la API: api-energi-ai

**6. EJEMPLOS DE PETICIONES (cURL)**

---

# Petición POST:
curl -X POST http://localhost:8082/api/analisis-energetico -H "Content-Type: application/json" -d "{\"consumo_kwh\": 250.5, \"tipo_inmueble\": \"RESIDENCIAL\", \"uso_horario_pico\": true, \"cantidad_equipos\": 3, \"horas_alto_consumo\": 4}"

# Petición GET:
curl -X GET http://localhost:8082/api/analisis/1
    * *(Datos por agregar).*

---
