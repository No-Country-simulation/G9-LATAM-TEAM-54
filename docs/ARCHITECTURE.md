# Arquitectura del Sistema - G9-LATAM-TEAM-54

## 1. Enfoque General
El backend está estructurado bajo principios de arquitectura limpia y bajo acoplamiento utilizando **Spring Boot**. El flujo de negocio principal se divide en componentes especializados para garantizar la mantenibilidad y la escalabilidad del sistema.

## 2. Desglose de Servicios
La lógica de negocio se encuentra desacoplada en los siguientes servicios principales:

* **`AnalisisService`**: Actúa como el coordinador principal (*Orquestador*). Se encarga de recibir los datos de entrada, invocar secuencialmente al modelo de predicción, calcular los costos financieros y recopilar las recomendaciones pertinentes para empaquetarlas en una respuesta unificada.
* **`PredictionService`**: Encargado de gestionar el ciclo de vida del modelo de aprendizaje automático. Carga el archivo de modelo exportado e inicia la inferencia nativa en la JVM utilizando **ONNX Runtime** (`ai.onnxruntime`), optimizando el rendimiento sin necesidad de microservicios externos en Python.
* **`CostService`**: Motor financiero responsable de calcular el impacto económico multiplicando el consumo estimado por la tarifa base establecida ($0,75).
* **`RecommendationsService`**: Motor de reglas condicionales que evalúa el consumo actual y las proyecciones futuras para emitir sugerencias de optimización energética de manera dinámica.

## 3. Flujo de Datos
1. El controlador recibe la petición HTTP con los datos de entrada.
2. `AnalisisService` recibe los datos y solicita la inferencia a `PredictionService`.
3. Con el resultado de la predicción, se ejecutan en paralelo el cálculo en `CostService` y la evaluación de reglas en `RecommendationsService`.
4. El sistema consolida los resultados en un formato estructurado (`Map<String, Object>`) listo para la respuesta del cliente.
