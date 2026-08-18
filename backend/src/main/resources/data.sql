-- 1. Aires Acondicionados (Tienen variantes por BTU)
INSERT INTO equipo_catalogo (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
VALUES (1, 'Aire Acondicionado', 'Climatización', true, NULL)
    ON CONFLICT (id) DO NOTHING;

-- Variantes de BTU para el Aire Acondicionado
INSERT INTO equipo_variante (id, equipo_catalogo_id, etiqueta, potencia_watts)
VALUES (1, 1, '9,000 BTU', 900.0)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO equipo_variante (id, equipo_catalogo_id, etiqueta, potencia_watts)
VALUES (2, 1, '12,000 BTU', 1200.0)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO equipo_variante (id, equipo_catalogo_id, etiqueta, potencia_watts)
VALUES (3, 1, '18,000 BTU', 1800.0)
    ON CONFLICT (id) DO NOTHING;

-- 2. Nevera (Potencia fija, sin variantes)
INSERT INTO equipo_catalogo (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
VALUES (2, 'Nevera / Refrigerador', 'Línea Blanca', false, 250.0)
    ON CONFLICT (id) DO NOTHING;

-- 3. Televisor (Potencia fija, sin variantes)
INSERT INTO equipo_catalogo (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
VALUES (3, 'Televisor LED 50"', 'Entretenimiento', false, 120.0)
    ON CONFLICT (id) DO NOTHING;

-- 4. Bombillo LED estándar
INSERT INTO equipo_catalogo (id, nombre, categoria_uso, tiene_variantes, potencia_base_watts)
VALUES (4, 'Bombillo LED', 'Iluminación', false, 15.0)
    ON CONFLICT (id) DO NOTHING;