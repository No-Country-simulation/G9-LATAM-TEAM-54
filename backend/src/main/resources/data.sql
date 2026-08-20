-- 1. Opciones de Temperatura:

INSERT INTO "opciones_temperatura" (id, activo, etiqueta, orden, valor_numerico)
SELECT 1, true, 'Fría (~12º)', 1, 12
    WHERE NOT EXISTS (SELECT 1 FROM "opciones_temperatura" WHERE id = 1);

INSERT INTO "opciones_temperatura" (id, activo, etiqueta, orden, valor_numerico)
SELECT 2, true, 'Templado (~20º)', 2, 20
    WHERE NOT EXISTS (SELECT 1 FROM "opciones_temperatura" WHERE id = 2);

INSERT INTO "opciones_temperatura" (id, activo, etiqueta, orden, valor_numerico)
SELECT 3, true, 'Calido (~28º)', 3, 28
    WHERE NOT EXISTS (SELECT 1 FROM "opciones_temperatura" WHERE id = 3);

INSERT INTO "opciones_temperatura" (id, activo, etiqueta, orden, valor_numerico)
SELECT 4, true, 'Caluroso (~30º)', 4, 32
    WHERE NOT EXISTS (SELECT 1 FROM "opciones_temperatura" WHERE id = 4);

INSERT INTO "opciones_temperatura" (id, activo, etiqueta, orden, valor_numerico)
SELECT 5, true, 'Muy Caluroso (~35º)', 5, 35
    WHERE NOT EXISTS (SELECT 1 FROM "opciones_temperatura" WHERE id = 5);

-- 2. Catalogo de Equipos:

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 1, 'Aire Acondicionado', 'Climatización', true, NULL
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 1);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 2, 'Refrigerador / Nevera', 'Línea Blanca', false, 150.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 2);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 3, 'Bombillo LED', 'Iluminación', false, 10.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 3);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 4, 'Televisor', 'Entretenimiento', false, 120.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 4);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 5, 'Lavadora de Ropa', 'Línea Blanca', true, NULL
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 5);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 6, 'Microondas', 'Cocina', false, 1200.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 6);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 7, 'Computadora de Escritorio', 'Oficina / Hogar', false, 250.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 7);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 8, 'Ventilador de Techo / Piso', 'Climatización', true, NULL
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 8);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 9, 'Plancha para Ropa', 'Hogar', false, 1000.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 9);

INSERT INTO "equipo_catalogo" (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
SELECT 10, 'Router Wi-Fi', 'Tecnología', false, 15.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_catalogo" WHERE id = 10);

-- 3. Variantes de Equipos:

INSERT INTO "equipo_variante" (id, equipo_catalogo_id, etiqueta, potencia_watts)
SELECT 1, 1, '9,000 BTU', 900.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_variante" WHERE id = 1);

INSERT INTO "equipo_variante" (id, equipo_catalogo_id, etiqueta, potencia_watts)
SELECT 2, 1, '12,000 BTU', 1200.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_variante" WHERE id = 2);

INSERT INTO "equipo_variante" (id, equipo_catalogo_id, etiqueta, potencia_watts)
SELECT 3, 1, '18,000 BTU', 1800.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_variante" WHERE id = 3);

INSERT INTO "equipo_variante" (id, equipo_catalogo_id, etiqueta, potencia_watts)
SELECT 4, 1, '24,000 BTU', 2400.0
    WHERE NOT EXISTS (SELECT 1 FROM "equipo_variante" WHERE id = 4);

-- 4. Estancias:

INSERT INTO "estancias" (id, nombre)
SELECT 1, 'Sala / Sala de Estar'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 1);

INSERT INTO "estancias" (id, nombre)
SELECT 2, 'Comedor'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 2);

INSERT INTO "estancias" (id, nombre)
SELECT 3, 'Cocina'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 3);

INSERT INTO "estancias" (id, nombre)
SELECT 4, 'Dormitorio Principal'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 4);

INSERT INTO "estancias" (id, nombre)
SELECT 5, 'Dormitorio Secundario'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 5);

INSERT INTO "estancias" (id, nombre)
SELECT 6, 'Baño Principal'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 6);

INSERT INTO "estancias" (id, nombre)
SELECT 7, 'Baño de Visitas'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 7);

INSERT INTO "estancias" (id, nombre)
SELECT 8, 'Oficina / Estudio'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 8);

INSERT INTO "estancias" (id, nombre)
SELECT 9, 'Lavadero / Área de Servicio'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 9);

INSERT INTO "estancias" (id, nombre)
SELECT 10, 'Balcón / Terraza'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 10);

INSERT INTO "estancias" (id, nombre)
SELECT 11, 'Garaje / Estacionamiento'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 11);

INSERT INTO "estancias" (id, nombre)
SELECT 12, 'Pasillo / Hall de Entrada'
    WHERE NOT EXISTS (SELECT 1 FROM "estancias" WHERE id = 12);