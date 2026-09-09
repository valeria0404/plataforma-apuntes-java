package com.universidad.apuntes.model;

public class Usuario {
    private final String id;
    private String nombre;
    private String carrera;

    public Usuario(String id, String nombre, String carrera) {
        validarCampoObligatorio(id, "El ID del usuario es obligatorio");
        this.id = id.trim();
        setNombre(nombre);
        setCarrera(carrera);
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

    public void setNombre(String nombre) {
        validarCampoObligatorio(
                nombre,
                "El nombre del usuario es obligatorio"
        );

        this.nombre = nombre.trim();
    }

    public void setCarrera(String carrera) {
        validarCampoObligatorio(
                carrera,
                "La carrera del usuario es obligatoria"
        );

        this.carrera = carrera.trim();
    }

    private void validarCampoObligatorio(
            String valor,
            String mensaje) {

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