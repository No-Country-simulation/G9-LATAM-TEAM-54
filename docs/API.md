# Documentación de API - Endpoints

## Análisis Energético

### Ejecutar Análisis Completo
* **URL:** `/api/v1/analisis` *(Sujeto a cambios según la definición del controlador)*
* **Método:** `POST`
* **Descripción:** Orquesta la predicción mediante IA, calcula el costo financiero y genera recomendaciones basadas en los datos suministrados.

#### Respuesta de Ejemplo (`JSON`)
```json
{
  "consumoActual": 45.5,
  "prediccion": 120.3,
  "costoEstimado": 34.12,
  "recomendaciones": [
    "El consumo se encuentra dentro de los rangos normales optimizados.",
    "Alerta: La predicción futura indica un incremento notable en la demanda energética."
  ]
}
```
