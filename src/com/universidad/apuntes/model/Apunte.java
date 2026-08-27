package com.universidad.apuntes.model;

public class Apunte {
    private String id;
    private String titulo;
    private String materia;
    private String contenidoUrl;
    private Usuario autor;
    private int valoraciones;

    public Apunte(String id, String titulo, String materia, String contenidoUrl, Usuario autor) {
        this.id = id;
        this.titulo = titulo;
        this.materia = materia;
        this.contenidoUrl = contenidoUrl;
        this.autor = autor;
        this.valoraciones = 0;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getMateria() { return materia; }
    public String getContenidoUrl() { return contenidoUrl; }
    public Usuario getAutor() { return autor; }
    public int getValoraciones() { return valoraciones; }

    public void valorar() { this.valoraciones++; }

    @Override
    public String toString() {
        return "[" + materia.toUpperCase() + "] " + titulo + " | Autor: " + autor.getNombre() + " (" + valoraciones + " ⭐)";
    }
}