package com.universidad.apuntes.model;

public class Usuario {

    private String id;
    private String nombre;
    private String carrera;

    public Usuario(String id, String nombre, String carrera) {
        validarCampoObligatorio(id, "El ID del usuario es obligatorio");
        validarCampoObligatorio(nombre, "El nombre del usuario es obligatorio");
        validarCampoObligatorio(carrera, "La carrera del usuario es obligatoria");

        this.id = id.trim();
        this.nombre = nombre.trim();
        this.carrera = carrera.trim();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    private void validarCampoObligatorio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", carrera='" + carrera + '\'' +
                '}';
    }
}