# ⚡ EnergiAI – Inteligencia para el Consumo Energético

## Descripción

EnergiAI es una solución inteligente que analiza patrones de consumo de energía eléctrica para clasificar el nivel de eficiencia energética de un inmueble, estimar el costo mensual del consumo y generar recomendaciones personalizadas para promover hábitos de uso más sostenibles.

El sistema utiliza un modelo de Machine Learning entrenado con datos de consumo energético y expone sus funcionalidades mediante una API REST desarrollada con Spring Boot.

---

## Objetivos

* Analizar patrones de consumo energético.
* Clasificar el perfil energético del usuario.
* Generar recomendaciones para mejorar la eficiencia.
* Estimar el costo mensual del consumo eléctrico.
* Exponer los resultados mediante una API REST.
* Utilizar Oracle Cloud Infrastructure (OCI) como parte de la arquitectura.

---

## Tecnologías

### Backend

* Java 21
* Spring Boot 3
* Maven
* Spring Validation
* Swagger / OpenAPI

### Frontend

* HTML
* CSS
* JavaScript

### Ciencia de Datos

* Python
* Pandas
* NumPy
* Scikit-Learn
* Joblib

### Cloud

* Oracle Cloud Infrastructure (OCI)
* Object Storage

### Control de versiones

* Git
* GitHub

---

# Arquitectura

```text
                  Cliente
                     │
                     ▼
             Spring Boot REST API
                     │
                     ▼
          Modelo de Machine Learning
                     │
                     ▼
         OCI Object Storage (modelo)
```

---

# Estructura del proyecto

```text
EnergiAI/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── data-science/
│   ├── notebooks/
│   ├── dataset/
│   ├── models/
│   └── requirements.txt
│
├── oci/
│
└── README.md
```

---

# Funcionalidades

* Clasificación del consumo energético.
* Cálculo del costo estimado mensual.
* Recomendaciones para optimizar el consumo.
* Validación de datos de entrada.
* Documentación automática con Swagger.
* Integración con OCI Object Storage.

---

# API REST

## POST /analisis-energetico

Analiza el consumo energético enviado por el usuario.

### Request

```json
{
  "consumo_kwh": 420,
  "uso_horario_pico": true,
  "cantidad_equipos": 10,
  "tipo_inmueble": "Casa",
  "horas_alto_consumo": 8
}
```

### Response

```json
{
  "categoria": "Ineficiente",
  "probabilidad": 0.81,
  "costo_estimado_mensual": 315.00,
  "recomendaciones": [
    "Reducir el uso de equipos durante horarios pico",
    "Evaluar aparatos con alto consumo energético",
    "Distribuir actividades de mayor consumo a lo largo del día"
  ]
}
```

---

## GET /analisis/{id}

Consulta el resultado de un análisis previamente realizado.

---

# Ejecución del Backend

```bash
cd backend
```

```bash
mvn spring-boot:run
```

La API estará disponible en:

```text
http://localhost:8080
```

---

# Documentación de la API

Swagger estará disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Ciencia de Datos

El modelo de Machine Learning realiza las siguientes etapas:

* Exploración y limpieza de datos (EDA).
* Transformación de variables.
* Entrenamiento del modelo.
* Evaluación mediante métricas de clasificación.
* Serialización del modelo entrenado.

---

# Integración con OCI

El proyecto utiliza **Oracle Cloud Infrastructure Object Storage** para almacenar el modelo de Machine Learning utilizado por la API.

---

# Equipo

* Integrante 1 – 
* Integrante 2 – 
* Integrante 3 –
* Integrante 4 – 
* Integrante 5 – 
* Integrante 6 –
* Integrante 7 –
* Integrante 8 –
---

# Licencia

Proyecto desarrollado con fines académicos para el Hackathon de Oracle Cloud Infrastructure.
