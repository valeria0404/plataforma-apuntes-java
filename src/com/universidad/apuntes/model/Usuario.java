package com.universidad.apuntes.model;

public class Usuario {
    private String id;
    private String nombre;
    private String carrera;

    public Usuario(String id, String nombre, String carrera) {
        this.id = id;
        this.nombre = nombre;
        this.carrera = carrera;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCarrera() { return carrera; }
}