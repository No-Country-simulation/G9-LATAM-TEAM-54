# ⚡ EnergiAI — Inteligencia para el Consumo Energético

> Plataforma full-stack que analiza patrones de consumo eléctrico, clasifica la eficiencia energética de un inmueble, estima su costo mensual y genera recomendaciones personalizadas impulsadas por Machine Learning.

---

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Objetivos](#objetivos)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Ejecución con Docker Compose](#ejecución-con-docker-compose-recomendado)
- [Ejecución en Local (Desarrollo)](#ejecución-en-local-desarrollo)
- [Variables de Entorno](#variables-de-entorno)
- [API REST](#api-rest)
- [Ciencia de Datos](#ciencia-de-datos)
- [Equipo](#equipo)
- [Licencia](#licencia)

---

<a id="descripcion"></a>
## 📖 Descripción

**EnergiAI** es una solución inteligente desarrollada para el Hackathon de Oracle Cloud Infrastructure. El sistema integra un modelo de Machine Learning (exportado a formato **ONNX** y ejecutado nativamente en la JVM) con una API REST en Spring Boot y una interfaz de usuario en Vue 3.

Los usuarios pueden registrarse, ingresar sus datos de consumo eléctrico y obtener en tiempo real:

- 🏷️ **Clasificación energética**: Eficiente, Moderado o Ineficiente.
- 💰 **Costo mensual estimado** basado en tarifa de referencia.
- 💡 **Recomendaciones personalizadas** para reducir el consumo.
- 📊 **Dashboard** con historial, tendencias y desglose por estancia/dispositivo.

---

<a id="objetivos"></a>
## 🎯 Objetivos

- Analizar patrones de consumo energético mediante Machine Learning.
- Clasificar el perfil energético del usuario en tiempo real.
- Generar recomendaciones para mejorar la eficiencia.
- Estimar el costo mensual del consumo eléctrico.
- Exponer los resultados mediante una API REST documentada (Scalar/OpenAPI).
- Desplegar la solución en contenedores Docker sobre Oracle Cloud Infrastructure (OCI).

---

<a id="tecnologias"></a>
## 🛠️ Tecnologías

### Backend

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.3.2 | Framework web |
| Spring Security + JWT (JJWT) | 0.11.5 | Autenticación y autorización |
| Spring Data JPA | — | Persistencia de datos |
| ONNX Runtime | 1.28.0 | Inferencia nativa del modelo ML |
| PostgreSQL | 15 | Base de datos relacional |
| Lombok | — | Reducción de boilerplate |
| Springdoc OpenAPI / Scalar | 2.5.0 | Documentación interactiva de la API |
| Maven | 3.x | Gestión de dependencias y build |

### Frontend

| Tecnología | Versión | Uso |
|---|---|---|
| Vue 3 | ^3.5 | Framework reactivo |
| Vite | ^8.x | Bundler y servidor de desarrollo |
| Tailwind CSS | ^3.4 | Estilos utilitarios |
| DaisyUI | ^5.x | Componentes UI sobre Tailwind |
| Chart.js + vue-chartjs | ^4.5 | Gráficos y visualizaciones |
| Axios | ^1.19 | Cliente HTTP |

### Ciencia de Datos

| Tecnología | Uso |
|---|---|
| Python 3 | Lenguaje del notebook |
| Pandas + NumPy | EDA y transformación de datos |
| Scikit-Learn (Random Forest) | Entrenamiento del modelo de clasificación |
| skl2onnx / ONNX | Exportación del modelo para la JVM |
| Joblib | Serialización de artefactos |

### Infraestructura

| Tecnología | Uso |
|---|---|
| Docker + Docker Compose | Orquestación de contenedores |
| Adminer | Interfaz web de administración de PostgreSQL |
| Oracle Cloud Infrastructure (OCI) | Plataforma de despliegue |

---

<a id="arquitectura"></a>
## 🏗️ Arquitectura

```text
                        ┌─────────────────────────────┐
                        │         Navegador           │
                        │  Vue 3 + Tailwind + DaisyUI │
                        │     (Puerto 8084)           │
                        └──────────────┬──────────────┘
                                       │ HTTP / Axios
                        ┌──────────────▼───────────────┐
                        │      Spring Boot REST API    │
                        │    Spring Security + JWT     │
                        │         (Puerto 8082)        │
                        │                              │
                        │  ┌──────────────────────┐    │
                        │  │   AnalisisService    │    │
                        │  │  (Orquestador)       │    │
                        │  └──────┬───────┬───────┘    │
                        │         │       │            │
                        │  ┌──────▼──┐ ┌──▼──────────┐ │
                        │  │Predict  │ │ Cost +      │ │
                        │  │Service  │ │ Recommen.   │ │
                        │  │(ONNX)   │ │ Service     │ │
                        │  └─────────┘ └─────────────┘ │
                        └──────────────┬───────────────┘
                                       │
                        ┌──────────────▼───────────────┐
                        │  PostgreSQL 15 (Puerto 5432) │
                        │  Historial de análisis,      │
                        │  usuarios, dispositivos      │
                        └──────────────────────────────┘
```

### Servicios del Backend

| Servicio | Responsabilidad |
|---|---|
| `AnalisisService` | Orquestador principal: coordina predicción, costos y recomendaciones |
| `PredictionService` | Carga y ejecuta el modelo ONNX nativamente en la JVM |
| `CostService` | Calcula el costo mensual estimado (tarifa: \$0.75/kWh) |
| `RecommendationsService` | Motor de reglas que genera sugerencias de optimización |
| `DashboardService` | Agrega estadísticas e historial del usuario |
| `DispositivoService` / `EquipoService` | Gestión del catálogo de dispositivos y estancias |
| `UserService` | Gestión de usuarios y perfil |

---

<a id="estructura-del-proyecto"></a>
## 📁 Estructura del Proyecto

```text
G9-LATAM-TEAM-54/
│
├── .env                          ← Variables de entorno (no commitear)
├── .env.example                  ← Plantilla de variables de entorno
├── docker-compose.yaml           ← Orquestación de todos los servicios
├── mkdocs.yml                    ← Configuración de la documentación técnica
├── Notebook_Hackaton_grupo_54.ipynb  ← Notebook de ciencia de datos
│
├── backend/                      ← API REST Spring Boot
│   ├── src/main/java/com/energiai/backend/
│   │   ├── config/               ← CORS, Security, JWT, DataInitializer
│   │   ├── controller/           ← AnalisisController, AuthController,
│   │   │                            DashboardController, DispositivoController...
│   │   ├── dto/
│   │   │   ├── request/          ← AnalisisRequest, AuthRequest, ...
│   │   │   └── response/         ← AnalisisResponse, DashboardResponse, ...
│   │   ├── exception/            ← GlobalExceptionHandler
│   │   ├── model/                ← Entidades JPA (User, AnalisisEntity, ...)
│   │   ├── repository/           ← Spring Data JPA Repositories
│   │   ├── scheduler/            ← ReporteScheduler (tareas programadas)
│   │   └── service/              ← Lógica de negocio y servicios ML
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/                     ← SPA Vue 3
│   ├── src/
│   │   ├── api/                  ← Integración con la API REST (Axios)
│   │   ├── components/
│   │   │   ├── auth/             ← Login / Registro
│   │   │   ├── dashboard/        ← Panel principal
│   │   │   ├── devices/          ← Gestión de dispositivos
│   │   │   ├── reports/          ← Reportes y análisis
│   │   │   ├── trends/           ← Tendencias de consumo
│   │   │   └── layout/           ← Navbar, Sidebar, Footer
│   │   ├── App.vue
│   │   └── main.js
│   ├── Dockerfile
│   └── package.json
│
└── docs/                         ← Documentación técnica (MkDocs)
    ├── ARCHITECTURE.md
    ├── API.md
    ├── PROGRESS.md
    └── index.md
```

---

<a id="requisitos-previos"></a>
## 📦 Requisitos Previos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (con WSL 2 en Windows)
- Git

Para desarrollo local adicional:
- Java 21 (JDK)
- Node.js `^22.18.0` o `>=24.12.0`
- Maven 3.x

---

<a id="ejecucion-con-docker-compose"></a>
## 🐳 Ejecución con Docker Compose (Recomendado)

Este es el método principal para correr todos los servicios del sistema.

### 1. Clonar el repositorio

```bash
git clone https://github.com/<organización>/G9-LATAM-TEAM-54.git
cd G9-LATAM-TEAM-54
```

### 2. Configurar las variables de entorno

```bash
cp .env.example .env
```

Edita el archivo `.env` con tus credenciales (ver sección [Variables de Entorno](#variables-de-entorno)).

### 3. Levantar todos los servicios

```bash
docker compose up --build
```

### 4. Acceder a los servicios

| Servicio | URL | Descripción |
|---|---|---|
| **Frontend** | http://localhost:8084 | Interfaz de usuario Vue 3 |
| **API REST** | http://localhost:8082 | Endpoints de la API |
| **Scalar (Docs API)** | http://localhost:8082/scalar.html | Documentación interactiva |
| **Adminer (BD)** | http://localhost:8083 | Interfaz de administración PostgreSQL |

---

<a id="ejecucion-en-local-desarrollo"></a>
## 💻 Ejecución en Local (Desarrollo)

### Backend

```bash
cd backend
mvn spring-boot:run
```

> La API estará disponible en `http://localhost:8082`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

> La aplicación estará disponible en `http://localhost:5173`

---

<a id="variables-de-entorno"></a>
## 🔑 Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto basándote en `.env.example`:

```env
# Base de datos
POSTGRES_USER=postgres
POSTGRES_PASSWORD=tu_contrasena_aqui
POSTGRES_DB=energiai_db

# Spring Boot datasource
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/energiai_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=tu_contrasena_aqui

# Seguridad JWT
JWT_SECRET=tu_jwt_secret_aqui_minimo_256_bits
```

> ⚠️ **Nunca** commitees el archivo `.env` al repositorio. Ya está incluido en `.gitignore`.

---

<a id="api-rest"></a>
## 🌐 API REST

La API requiere autenticación JWT para la mayoría de los endpoints. Primero debes registrarte o iniciar sesión para obtener un token.

### Autenticación

#### `POST /api/auth/register` — Registro de usuario

```json
{
  "username": "usuario@email.com",
  "password": "tu_password"
}
```

#### `POST /api/auth/login` — Inicio de sesión

```json
{
  "username": "usuario@email.com",
  "password": "tu_password"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Incluye el token en el header de las siguientes peticiones:
```
Authorization: Bearer <token>
```

---

### Análisis Energético

#### `POST /api/analisis-energetico` — Crear análisis

**Request:**
```json
{
  "consumo_kwh": 250.5,
  "tipo_inmueble": "RESIDENCIAL",
  "uso_horario_pico": true,
  "cantidad_equipos": 3,
  "horas_alto_consumo": 4
}
```

| Campo | Tipo | Descripción |
|---|---|---|
| `consumo_kwh` | `Double` | Energía consumida en kWh |
| `tipo_inmueble` | `String` | `"RESIDENCIAL"`, `"COMERCIAL"`, etc. |
| `uso_horario_pico` | `Boolean` | Si consume en horarios pico |
| `cantidad_equipos` | `Integer` | Número de artefactos eléctricos |
| `horas_alto_consumo` | `Integer` | Horas de uso intensivo por día |

**Response (200 OK):**
```json
{
  "id": 1,
  "categoria": "Moderado",
  "probabilidad": 0.82,
  "consumoActual": 250.5,
  "costoEstimado": 187.875,
  "recomendaciones": [
    "El consumo actual es elevado. Considere apagar equipos en standby.",
    "Redistribuya actividades de mayor consumo fuera del horario pico."
  ]
}
```

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | ID único del análisis en BD |
| `categoria` | `String` | `"Eficiente"`, `"Moderado"` o `"Ineficiente"` |
| `probabilidad` | `Double` | Confianza del modelo (0.0 – 1.0) |
| `costoEstimado` | `Double` | Costo mensual estimado en \$ |
| `recomendaciones` | `String[]` | Lista de sugerencias personalizadas |

---

#### `GET /api/analisis/{id}` — Consultar análisis por ID

Retorna el resultado de un análisis previamente realizado.

---

### Ejemplos con cURL

```bash
# Inicio de sesión
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user@example.com", "password": "password123"}'

# Crear análisis (reemplaza <TOKEN>)
curl -X POST http://localhost:8082/api/analisis-energetico \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"consumo_kwh": 250.5, "tipo_inmueble": "RESIDENCIAL", "uso_horario_pico": true, "cantidad_equipos": 3, "horas_alto_consumo": 4}'

# Consultar análisis por ID
curl -X GET http://localhost:8082/api/analisis/1 \
  -H "Authorization: Bearer <TOKEN>"
```

> 📖 Para la documentación completa e interactiva, accede a **[Scalar](http://localhost:8082/scalar.html)** con la aplicación corriendo.

---

<a id="ciencia-de-datos"></a>
## 🧪 Ciencia de Datos

El modelo de Machine Learning sigue el siguiente pipeline:

1. **EDA y limpieza**: Detección de nulos, duplicados, estandarización de formatos.
2. **Feature Engineering**: Variables de eficiencia, consumo per cápita, variaciones diarias.
3. **Entrenamiento**: Modelo **Random Forest** para clasificación en tres niveles de eficiencia.
4. **Evaluación**: Métricas de precision, recall, F1-score y matriz de confusión.
5. **Serialización y exportación**: El modelo se exporta a formato **ONNX** para su ejecución nativa en la JVM mediante ONNX Runtime, eliminando la necesidad de un microservicio Python en producción.

El notebook completo se encuentra en: [`Notebook_Hackaton_grupo_54.ipynb`](./Notebook_Hackaton_grupo_54.ipynb)

---

<a id="equipo"></a>
## 👥 Equipo — G9 LATAM Team 54

| Integrante | Rol |
|---|---|
| Integrante 1 | — |
| Integrante 2 | — |
| Integrante 3 | — |
| Integrante 4 | — |
| Integrante 5 | — |
| Integrante 6 | — |
| Integrante 7 | — |
| Integrante 8 | — |

---

<a id="licencia"></a>
## 📄 Licencia

Proyecto desarrollado con fines académicos para el **Hackathon de Oracle Cloud Infrastructure — G9 LATAM Team 54**.
