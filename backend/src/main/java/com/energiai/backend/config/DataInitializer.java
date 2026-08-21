package com.energiai.backend.config;

import com.energiai.backend.model.EquipoCatalogo;
import com.energiai.backend.model.EquipoVariante;
import com.energiai.backend.model.Estancia;
import com.energiai.backend.model.OpcionTemperatura;
import com.energiai.backend.repository.EquipoCatalogoRepository;
import com.energiai.backend.repository.EquipoVarianteRepository;
import com.energiai.backend.repository.EstanciaRepository;
import com.energiai.backend.repository.OpcionTemperaturaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            OpcionTemperaturaRepository opcionTemperaturaRepository,
            EquipoCatalogoRepository equipoCatalogoRepository,
            EquipoVarianteRepository equipoVarianteRepository,
            EstanciaRepository estanciaRepository) {

        return args -> {
            // 1. Opciones de Temperatura
            if (opcionTemperaturaRepository.count() == 0) {
                OpcionTemperatura t1 = new OpcionTemperatura();
                t1.setId(1L); t1.setActivo(true); t1.setEtiqueta("Fría (~12º)"); t1.setOrden(1); t1.setValorNumerico(12);
                opcionTemperaturaRepository.save(t1);

                OpcionTemperatura t2 = new OpcionTemperatura();
                t2.setId(2L); t2.setActivo(true); t2.setEtiqueta("Templado (~20º)"); t2.setOrden(2); t2.setValorNumerico(20);
                opcionTemperaturaRepository.save(t2);

                OpcionTemperatura t3 = new OpcionTemperatura();
                t3.setId(3L); t3.setActivo(true); t3.setEtiqueta("Calido (~28º)"); t3.setOrden(3); t3.setValorNumerico(28);
                opcionTemperaturaRepository.save(t3);

                OpcionTemperatura t4 = new OpcionTemperatura();
                t4.setId(4L); t4.setActivo(true); t4.setEtiqueta("Caluroso (~30º)"); t4.setOrden(4); t4.setValorNumerico(32);
                opcionTemperaturaRepository.save(t4);

                OpcionTemperatura t5 = new OpcionTemperatura();
                t5.setId(5L); t5.setActivo(true); t5.setEtiqueta("Muy Caluroso (~35º)"); t5.setOrden(5); t5.setValorNumerico(35);
                opcionTemperaturaRepository.save(t5);
            }

            // 2. Catálogo de Equipos
            if (equipoCatalogoRepository.count() == 0) {
                Object[][] equipos = {
                        {1L, "Aire Acondicionado", "Climatización", true, null},
                        {2L, "Refrigerador / Nevera", "Línea Blanca", false, 150.0},
                        {3L, "Bombillo LED", "Iluminación", false, 10.0},
                        {4L, "Televisor", "Entretenimiento", false, 120.0},
                        {5L, "Lavadora de Ropa", "Línea Blanca", true, null},
                        {6L, "Microondas", "Cocina", false, 1200.0},
                        {7L, "Computadora de Escritorio", "Oficina / Hogar", false, 250.0},
                        {8L, "Ventilador de Techo / Piso", "Climatización", true, null},
                        {9L, "Plancha para Ropa", "Hogar", false, 1000.0},
                        {10L, "Router Wi-Fi", "Tecnología", false, 15.0}
                };

                for (Object[] e : equipos) {
                    EquipoCatalogo eq = new EquipoCatalogo();
                    eq.setId((Long) e[0]);
                    eq.setNombre((String) e[1]);
                    eq.setCategoriaUso((String) e[2]);
                    eq.setTieneVariantes((Boolean) e[3]);
                    eq.setPotenciaBaseWatts((Double) e[4]);
                    equipoCatalogoRepository.save(eq);
                }
            }

            // 3. Variantes de Equipos
            if (equipoVarianteRepository.count() == 0) {
                EquipoCatalogo aireAcondicionado = equipoCatalogoRepository.findById(1L).orElse(null);
                if (aireAcondicionado != null) {
                    Object[][] variantes = {
                            {1L, "9,000 BTU", 900.0},
                            {2L, "12,000 BTU", 1200.0},
                            {3L, "18,000 BTU", 1800.0},
                            {4L, "24,000 BTU", 2400.0}
                    };
                    for (Object[] v : variantes) {
                        EquipoVariante ev = new EquipoVariante();
                        ev.setId((Long) v[0]);
                        ev.setEquipoCatalogo(aireAcondicionado);
                        ev.setEtiqueta((String) v[1]);
                        ev.setPotenciaWatts((Double) v[2]);
                        equipoVarianteRepository.save(ev);
                    }
                }
            }

            // 4. Estancias
            if (estanciaRepository.count() == 0) {
                String[] estanciasNombres = {
                        "Sala / Sala de Estar", "Comedor", "Cocina", "Dormitorio Principal",
                        "Dormitorio Secundario", "Baño Principal", "Baño de Visitas",
                        "Oficina / Estudio", "Lavadero / Área de Servicio", "Balcón / Terraza",
                        "Garaje / Estacionamiento", "Pasillo / Hall de Entrada"
                };
                for (int i = 0; i < estanciasNombres.length; i++) {
                    Estancia est = new Estancia();
                    est.setId((long) (i + 1));
                    est.setNombre(estanciasNombres[i]);
                    estanciaRepository.save(est);
                }
            }
        };
    }
}