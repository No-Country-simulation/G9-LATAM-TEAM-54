# 📊 Bitácora de Progreso e Hitos - G9-LATAM-TEAM-54

Este documento recopila el avance cronológico y el estado de las tareas del proyecto, categorizadas por áreas de desarrollo.

---

## 🤖 1. Data Science & Machine Learning

* **Objetivo:** Desarrollar un flujo de trabajo completo de análisis energético, capaz de explorar y limpiar los datos, identificar patrones relevantes de consumo, transformar variables clave, entrenar y evaluar un modelo supervisado para clasificar la eficiencia energética de los hogares, generar recomendaciones automatizadas y serializar el modelo para su uso futuro en aplicaciones o sistemas de monitoreo.
* **Avances técnicos:**
    * **Exploración y limpieza de datos (EDA):** revisión de estructura del dataset, tratamiento de valores nulos y duplicados, análisis descriptivo y transformación de la columna de fecha para extraer información temporal.
    * **Análisis de patrones de consumo:** estudio del comportamiento energético por día de la semana y por mes, evaluación del impacto del tamaño del hogar, temperatura y uso en horas punta.
    * **Procesamiento y transformación de variables:** creación de consumo per cápita, codificación de variables categóricas, definición de umbrales mediante percentiles y generación del perfil energético como variable objetivo.
    * **Entrenamiento del modelo supervisado:** división del dataset en entrenamiento y prueba, normalización de variables y entrenamiento de un modelo Random Forest para clasificar hogares en tres niveles de eficiencia.
    * **Evaluación del modelo:** análisis de métricas de desempeño (precision, recall, f1-score, matriz de confusión) para validar la capacidad del modelo de generalizar correctamente.
    * **Generación de recomendaciones:** implementación de reglas basadas en el perfil energético y en condiciones específicas como temperatura y uso de aire acondicionado, además de recomendaciones derivadas de las predicciones del modelo.
    * **Serialización del modelo:** guardado del modelo entrenado y del scaler para permitir su reutilización sin necesidad de reentrenamiento.

## 📊 REPORTE DE AVANCES 20-07-2026

### 1. Resumen de Cambios Realizados
* **Definición de dataset:** Luego de analizar el proyecto, se consultaron distintos repositorios de datasets. Se seleccionó el dataset `household_energy_consumption`, que presenta el consumo energético de varios hogares durante un mes y contempla variables como consumo total de energía, número de integrantes del hogar y consumo en horas punta, entre otras.

## 📊 REPORTE DE AVANCES 27-07-2026

### 1. Resumen de Cambios Realizados
* **Limpieza de datos y exploración inicial:** se creó un notebook y, luego de la importación de librerías y la carga del dataset, se procedió con la limpieza de los datos para detectar valores faltantes, duplicados y errores y estandarizar formatos como fechas y unidades de medida. A continuación, se realizó la exploración inicial, para identificar tendencias generales, como patrones de consumo por día de la semana y uso en horas punta, y se generaron visualizaciones preliminares.
* **Procesamiento y transformación de variables:** se crearon nuevas variables, como indicadores de eficiencia, consumo per cápita y variaciones diarias, y se seleccionaron variables relevantes para el modelo supervisado.
* **Entrenamiento del modelo supervisado:** se dividió el dataset en un grupo de prueba y otro de entrenamiento mediante train-test split y se entrenó el modelo Random Forest para clasificar hogares en tres niveles de eficiencia.
* **Evaluación del modelo:** se analizaron métricas de desempeño (precision, recall, f1-score,matriz de confusión) para validar el modelo y se creó un sistema de recomendaciones automatizadas basadas en reglas y predicciones del modelo.
* **Serialización del modelo:** se generan los archivos `modelo_eficiencia.pkl` y `scaler_eficiencia.pkl` para reutilizar el modelo sin necesidad de reentrenarlo, facilitando así su integración en aplicaciones.



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

---

## 📊 REPORTE DE AVANCES 07-08-2026

### 1. Resumen de Cambios Realizados
* **Persistencia y Base de Datos:** Se configuró PostgreSQL con volúmenes persistentes en Docker Compose, garantizando que los datos no se pierdan al apagar los contenedores.
* **Actualización del Backend (Spring Boot):** Se adaptaron las entidades y servicios: categoría del perfil energético (Eficiente, Moderado, Ineficiente), probabilidad y costo estimado basado en la tarifa de referencia de $0,75 por kWh.
* **Endpoints Operativos:**
    * `POST /api/analisis-energetico`: Procesa los datos de consumo, guarda el registro en la base de datos y devuelve el JSON requerido con su ID.
    * `GET /api/analisis/{id}`: Permite consultar el historial de un análisis específico desde la base de datos.

---

### 2. Documentación y Puerto de Pruebas (Scalar)
* El proyecto incluye integración con Scalar para la documentación interactiva de la API.
* Una vez que la aplicación esté corriendo, puedes acceder a la interfaz gráfica de Scalar en tu navegador en el siguiente puerto y ruta: `http://localhost:8082/scalar.html` (o según el puerto configurado en tu docker-compose).

---

## 📊 REPORTE DE AVANCES 09-08-2026

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
