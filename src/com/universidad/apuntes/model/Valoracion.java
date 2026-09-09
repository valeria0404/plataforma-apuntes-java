package com.universidad.apuntes.model;

public class Valoracion {

    private final String id;
    private final Usuario usuario;
    private final Apunte apunte;
    private int estrellas;

    public Valoracion(
            String id,
            Usuario usuario,
            Apunte apunte,
            int estrellas) {

        validarCampoObligatorio(
                id,
                "El ID de la valoracion es obligatorio"
        );

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario de la valoracion es obligatorio"
            );
        }

        if (apunte == null) {
            throw new IllegalArgumentException(
                    "El apunte de la valoracion es obligatorio"
            );
        }

        this.id = id.trim();
        this.usuario = usuario;
        this.apunte = apunte;

        setEstrellas(estrellas);
    }

    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Apunte getApunte() {
        return apunte;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {

        if (estrellas < 1 || estrellas > 5) {
            throw new IllegalArgumentException(
                    "Las estrellas deben estar entre 1 y 5"
            );
        }

        this.estrellas = estrellas;
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

        return "Valoracion{" +
                "id='" + id + '\'' +
                ", usuario=" + usuario.getId() +
                ", apunte=" + apunte.getId() +
                ", estrellas=" + estrellas +
                '}';
    }
}