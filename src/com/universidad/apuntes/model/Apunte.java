package com.universidad.apuntes.model;

public class Apunte {

    private String id;
    private String titulo;
    private String materia;
    private String contenidoUrl;
    private Usuario autor;
    private int valoraciones;

    public Apunte(String id, String titulo, String materia, String contenidoUrl, Usuario autor) {
        validarCampoObligatorio(id, "El ID del apunte es obligatorio");
        validarCampoObligatorio(titulo, "El titulo del apunte es obligatorio");
        validarCampoObligatorio(materia, "La materia del apunte es obligatoria");
        validarCampoObligatorio(contenidoUrl, "La URL del contenido es obligatoria");

        if (autor == null) {
            throw new IllegalArgumentException("El autor del apunte es obligatorio");
        }

        this.id = id.trim();
        this.titulo = titulo.trim();
        this.materia = materia.trim();
        this.contenidoUrl = contenidoUrl.trim();
        this.autor = autor;
        this.valoraciones = 0;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMateria() {
        return materia;
    }

    public String getContenidoUrl() {
        return contenidoUrl;
    }

    public Usuario getAutor() {
        return autor;
    }

    public int getValoraciones() {
        return valoraciones;
    }

    public void valorar() {
        this.valoraciones++;
    }

    private void validarCampoObligatorio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    @Override
    public String toString() {
        return "[" + materia.toUpperCase() + "] "
                + titulo
                + " | Autor: "
                + autor.getNombre()
                + " (" + valoraciones + " ⭐)";
    }
}