package com.energiai.backend.dto.response;

public class UserResponse {
    private Long id;
    private String email;
    private String nombre;
    private Integer householdSize;
    private Double avgTemperatureC;

    public UserResponse(Long id, String email, String nombre, Integer householdSize, Double avgTemperatureC) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.householdSize = householdSize;
        this.avgTemperatureC = avgTemperatureC;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public Integer getHouseholdSize() { return householdSize; }
    public Double getAvgTemperatureC() { return avgTemperatureC; }
}