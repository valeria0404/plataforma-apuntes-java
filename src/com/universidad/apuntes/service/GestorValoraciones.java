package com.universidad.apuntes.service;

import com.universidad.apuntes.model.Valoracion;

import java.util.ArrayList;
import java.util.List;

public class GestorValoraciones {

    private final List<Valoracion> valoraciones;

    public GestorValoraciones() {
        this.valoraciones = new ArrayList<>();
    }

    // CREATE
    public boolean crear(
            Valoracion valoracion) {

        if (valoracion == null
                || buscarPorId(
                valoracion.getId()) != null) {

            return false;
        }

        valoraciones.add(valoracion);

        valoracion
                .getApunte()
                .incrementarValoraciones();

        return true;
    }

    // READ - TODAS
    public List<Valoracion> listar() {
        return new ArrayList<>(valoraciones);
    }

    // READ - POR ID
    public Valoracion buscarPorId(String id) {

        if (id == null) {
            return null;
        }

        for (Valoracion valoracion :
                valoraciones) {

            if (valoracion.getId()
                    .equalsIgnoreCase(id.trim())) {

                return valoracion;
            }
        }

        return null;
    }

    // UPDATE
    public boolean actualizar(
            String id,
            int estrellas) {

        Valoracion valoracion =
                buscarPorId(id);

        if (valoracion == null) {
            return false;
        }

        valoracion.setEstrellas(estrellas);

        return true;
    }

    // DELETE
    public boolean eliminar(String id) {

        Valoracion valoracion =
                buscarPorId(id);

        if (valoracion == null) {
            return false;
        }

        valoraciones.remove(valoracion);

        valoracion
                .getApunte()
                .decrementarValoraciones();

        return true;
    }

    public boolean existePorUsuario(
            String usuarioId) {

        for (Valoracion valoracion :
                valoraciones) {

            if (valoracion.getUsuario()
                    .getId()
                    .equalsIgnoreCase(usuarioId)) {

                return true;
            }
        }

        return false;
    }

    public boolean existePorApunte(
            String apunteId) {

        for (Valoracion valoracion :
                valoraciones) {

            if (valoracion.getApunte()
                    .getId()
                    .equalsIgnoreCase(apunteId)) {

                return true;
            }
        }

        return false;
    }
}