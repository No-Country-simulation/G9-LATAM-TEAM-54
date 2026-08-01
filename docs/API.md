# 🔌 Documentación de la API

!!! warning "Advertencia de desarrollo"
    Los endpoints de la API pueden cambiar según la última actualización del controlador del backend.

---

## Análisis Energético

### Ejecutar Análisis Completo

Endpoint encargado de orquestar la predicción mediante inteligencia artificial, calcular el costo financiero estimado y generar recomendaciones automatizadas.

* **URL:** `/api/v1/analisis`
* **Método:** `HTTP POST`
* **Content-Type:** `application/json`

---

### Ejemplo de Uso

=== "Petición (Request)"

    ```json
    {
      "consumo_kwh": 420,
      "uso_horario_pico": true,
      "cantidad_equipos": 10,
      "tipo_inmueble": "Casa",
      "horas_alto_consumo": 8
    }
    ```

=== "Respuesta Exitosa (200 OK)"

    ```json
    {
      "consumoActual": 420.0,
      "prediccion": 450.3,
      "costoEstimado": 315.00,
      "recomendaciones": [
        "Reducir el uso de equipos durante los horarios pico.",
        "Distribuir las actividades de mayor consumo a lo largo del día."
      ]
    }
    ```

---

### Estructura de Datos

| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `consumo_kwh` | `Float` | Consumo energético actual registrado. |
| `uso_horario_pico` | `Boolean` | Indica si hay alto uso en horas pico (`true` / `false`). |
| `cantidad_equipos` | `Integer` | Número de dispositivos eléctricos activos en el inmueble. |
| `tipo_inmueble` | `String` | Categoría de la locación (ej. Casa, Departamento). |
| `horas_alto_consumo` | `Integer` | Tiempo estimado de uso intensivo diario. |
