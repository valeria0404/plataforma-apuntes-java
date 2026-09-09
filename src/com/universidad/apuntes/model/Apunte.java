package com.universidad.apuntes.model;

public class Apunte {

    private final String id;
    private String titulo;
    private String materia;
    private String contenidoUrl;
    private Usuario autor;
    private int valoraciones;

    public Apunte(
            String id,
            String titulo,
            String materia,
            String contenidoUrl,
            Usuario autor) {

        validarCampoObligatorio(
                id,
                "El ID del apunte es obligatorio"
        );

        this.id = id.trim();

        setTitulo(titulo);
        setMateria(materia);
        setContenidoUrl(contenidoUrl);
        setAutor(autor);

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

    public void setTitulo(String titulo) {
        validarCampoObligatorio(
                titulo,
                "El titulo del apunte es obligatorio"
        );

        this.titulo = titulo.trim();
    }

    public void setMateria(String materia) {
        validarCampoObligatorio(
                materia,
                "La materia del apunte es obligatoria"
        );

        this.materia = materia.trim();
    }

    public void setContenidoUrl(String contenidoUrl) {
        validarCampoObligatorio(
                contenidoUrl,
                "La URL del contenido es obligatoria"
        );

        this.contenidoUrl = contenidoUrl.trim();
    }

    public void setAutor(Usuario autor) {

        if (autor == null) {
            throw new IllegalArgumentException(
                    "El autor del apunte es obligatorio"
            );
        }

        this.autor = autor;
    }

    public void incrementarValoraciones() {
        this.valoraciones++;
    }

    public void decrementarValoraciones() {

        if (this.valoraciones > 0) {
            this.valoraciones--;
        }
    }

    public void valorar() {
        incrementarValoraciones();
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

        return "[" + materia.toUpperCase() + "] "
                + titulo
                + " | Autor: "
                + autor.getNombre()
                + " (" + valoraciones
                + " valoraciones)";
    }
}