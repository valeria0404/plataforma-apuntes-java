package com.universidad.apuntes.service;

import com.universidad.apuntes.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

    private final List<Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
    }

    // CREATE
    public boolean crear(Usuario usuario) {

        if (usuario == null
                || buscarPorId(usuario.getId()) != null) {

            return false;
        }

        usuarios.add(usuario);
        return true;
    }

    // READ - TODOS
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios);
    }

    // READ - POR ID
    public Usuario buscarPorId(String id) {

        if (id == null) {
            return null;
        }

        for (Usuario usuario : usuarios) {

            if (usuario.getId()
                    .equalsIgnoreCase(id.trim())) {

                return usuario;
            }
        }

        return null;
    }

    // UPDATE
    public boolean actualizar(
            String id,
            String nombre,
            String carrera) {

        Usuario usuario = buscarPorId(id);

        if (usuario == null) {
            return false;
        }

        usuario.setNombre(nombre);
        usuario.setCarrera(carrera);

        return true;
    }

    // DELETE
    public boolean eliminar(String id) {

        Usuario usuario = buscarPorId(id);

        if (usuario == null) {
            return false;
        }

        return usuarios.remove(usuario);
    }
}