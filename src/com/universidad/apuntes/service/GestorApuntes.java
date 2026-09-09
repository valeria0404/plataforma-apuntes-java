package com.universidad.apuntes.service;

import com.universidad.apuntes.model.Apunte;
import com.universidad.apuntes.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorApuntes {

    private final List<Apunte> catalogo;

    public GestorApuntes() {
        this.catalogo = new ArrayList<>();
    }

    // CREATE
    public boolean publicarApunte(Apunte apunte) {

        if (apunte == null
                || buscarPorId(apunte.getId()) != null) {

            return false;
        }

        catalogo.add(apunte);

        return true;
    }

    // READ - TODOS
    public List<Apunte> listar() {
        return new ArrayList<>(catalogo);
    }

    // READ - POR ID
    public Apunte buscarPorId(String id) {

        if (id == null) {
            return null;
        }

        for (Apunte apunte : catalogo) {

            if (apunte.getId()
                    .equalsIgnoreCase(id.trim())) {

                return apunte;
            }
        }

        return null;
    }

    // READ - POR MATERIA
    public List<Apunte> buscarPorMateria(
            String materia) {

        List<Apunte> resultados =
                new ArrayList<>();

        if (materia == null) {
            return resultados;
        }

        for (Apunte apunte : catalogo) {

            if (apunte.getMateria()
                    .equalsIgnoreCase(materia.trim())) {

                resultados.add(apunte);
            }
        }

        return resultados;
    }

    // UPDATE
    public boolean actualizar(
            String id,
            String titulo,
            String materia,
            String contenidoUrl,
            Usuario autor) {

        Apunte apunte = buscarPorId(id);

        if (apunte == null) {
            return false;
        }

        apunte.setTitulo(titulo);
        apunte.setMateria(materia);
        apunte.setContenidoUrl(contenidoUrl);
        apunte.setAutor(autor);

        return true;
    }

    // DELETE
    public boolean eliminar(String id) {

        Apunte apunte = buscarPorId(id);

        if (apunte == null) {
            return false;
        }

        return catalogo.remove(apunte);
    }

    public boolean existePorAutor(
            String usuarioId) {

        for (Apunte apunte : catalogo) {

            if (apunte.getAutor()
                    .getId()
                    .equalsIgnoreCase(usuarioId)) {

                return true;
            }
        }

        return false;
    }
}