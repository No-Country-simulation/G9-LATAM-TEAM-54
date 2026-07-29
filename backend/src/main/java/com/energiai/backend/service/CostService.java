package com.energiai.backend.service;

import org.springframework.stereotype.Service;

@Service
public class CostService {

    private static final double TARIFA_BASE = 0.75;

    public double calcularCosto(double consumo) {
        return consumo * TARIFA_BASE;
    }
}