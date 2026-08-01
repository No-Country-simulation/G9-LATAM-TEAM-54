# Test File

Este archivo sirve para verificar cómo se renderizan los estilos, bloques de código y extensiones en Vercel.

## 1. Bloque de código JSON simple

```json
{
  "test": true,
  "mensaje": "Probando bloques de código en MkDocs"
}
```
---

## 2. Bloques de Advertencia y Notas (Admonitions)

MkDocs Material incluye cajas estilizadas para resaltar información importante:

??? note "Nota desplegable (Colapsable)"
    Este contenido está oculto por defecto y se despliega al hacer clic. Ideal para notas largas o ejemplos de código opcionales.

!!! warning "Advertencia importante"
    Los endpoints de la API pueden cambiar según la última actualización del controlador del backend.

!!! success "Éxito"
    La predicción de consumo energético se completó sin errores en el servidor.

## 3. Tablas Estilizadas

| Método HTTP | Endpoint | Descripción | Estado |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/status` | Verifica el estado del servicio | **Activo** |
| `POST` | `/api/v1/analisis` | Ejecuta la predicción de IA | **Estable** |

## 4. Código con pestañas (Tabbed Code Blocks)

Puedes agrupar múltiples lenguajes en una sola pestaña interactiva:

=== "Python"
    ```python
    import requests

    response = requests.post("http://localhost:8000/api/v1/analisis")
    print(response.json())
    ```

=== "JavaScript"
    ```javascript
    const response = await fetch('/api/v1/analisis', {
      method: 'POST'
    });
    const data = await response.json();
    console.log(data);
    ```

## 5. Texto Destacado y Teclado

Puedes resaltar texto importante ==como este marcador== o indicar atajos de teclado como ++ctrl+c++ para copiar.
