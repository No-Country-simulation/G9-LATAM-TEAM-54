# Bitácora de Progreso y Hitos - G9-LATAM-TEAM-54

Este documento recopila el avance cronológico y el estado de las tareas del proyecto, categorizadas por áreas de desarrollo.

---

## 🤖 1. Data Science & Machine Learning

(Datos por agregar).

---

## ☕ 2. Backend & Arquitectura (Spring Boot)

### Card #05: Implementación de DTOs (Completada)
* **Objetivo:** Crear las clases que representan las solicitudes y respuestas de la API, asegurando la integridad desde la entrada.
* **Avances técnicos:**
  * Creación de `AnalisisRequest` incorporando las validaciones de Jakarta (`@NotNull`, `@Min`, `@NotBlank`) para los campos clave.
  * Definición de `AnalisisResponse` estructurando los campos de salida (categoría, probabilidad, recomendaciones y costo estimado mensual).
  * Implementación de `ErrorResponse` para estandarizar el manejo de errores.

### Card #06: Lógica de Negocio y Servicios (Completada)
* **Objetivo:** Estructurar la arquitectura de servicios desacoplados para mantener el código limpio y mantenible.
* **Avances técnicos:**
  * Configuración de `PredictionService` para la ejecución del modelo ONNX de forma nativa en Spring Boot (`@PostConstruct` / `@PreDestroy`).
  * Desarrollo de `CostService` para el cálculo financiero.
  * Creación del motor de reglas en `RecommendationsService`.
  * Integración de la orquestación central en `AnalisisService`.

---
