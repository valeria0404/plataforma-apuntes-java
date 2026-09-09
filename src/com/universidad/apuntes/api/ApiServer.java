package com.universidad.apuntes.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import com.universidad.apuntes.model.Apunte;
import com.universidad.apuntes.model.Usuario;
import com.universidad.apuntes.model.Valoracion;

import com.universidad.apuntes.service.GestorApuntes;
import com.universidad.apuntes.service.GestorUsuarios;
import com.universidad.apuntes.service.GestorValoraciones;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.Executors;

public class ApiServer {

    private final HttpServer server;

    private final GestorUsuarios gestorUsuarios;
    private final GestorApuntes gestorApuntes;
    private final GestorValoraciones gestorValoraciones;

    public ApiServer(
            int puerto,
            GestorUsuarios gestorUsuarios,
            GestorApuntes gestorApuntes,
            GestorValoraciones gestorValoraciones)
            throws IOException {

        this.gestorUsuarios =
                gestorUsuarios;

        this.gestorApuntes =
                gestorApuntes;

        this.gestorValoraciones =
                gestorValoraciones;

        this.server =
                HttpServer.create(
                        new InetSocketAddress(puerto),
                        0
                );

        server.createContext(
                "/usuarios",
                new UsuariosHandler()
        );

        server.createContext(
                "/apuntes",
                new ApuntesHandler()
        );

        server.createContext(
                "/valoraciones",
                new ValoracionesHandler()
        );

        server.setExecutor(
                Executors.newCachedThreadPool()
        );
    }

    public void iniciar() {
        server.start();
    }

    public void detener() {
        server.stop(0);
    }

    // ============================
    // USUARIOS
    // ============================

    private class UsuariosHandler
            implements HttpHandler {

        @Override
        public void handle(
                HttpExchange exchange)
                throws IOException {

            try {

                String metodo =
                        exchange.getRequestMethod();

                String id =
                        obtenerId(
                                exchange,
                                "usuarios"
                        );

                switch (metodo) {

                    case "GET" ->
                            obtenerUsuarios(
                                    exchange,
                                    id
                            );

                    case "POST" ->
                            crearUsuario(
                                    exchange,
                                    id
                            );

                    case "PUT" ->
                            actualizarUsuario(
                                    exchange,
                                    id
                            );

                    case "DELETE" ->
                            eliminarUsuario(
                                    exchange,
                                    id
                            );

                    default ->
                            enviarJson(
                                    exchange,
                                    405,
                                    JsonUtil.error(
                                            "Metodo HTTP no permitido"
                                    )
                            );
                }

            } catch (
                    IllegalArgumentException e) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                e.getMessage()
                        )
                );

            } catch (Exception e) {

                enviarJson(
                        exchange,
                        500,
                        JsonUtil.error(
                                "Error interno del servidor"
                        )
                );
            }
        }

        private void obtenerUsuarios(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id == null) {

                enviarJson(
                        exchange,
                        200,
                        JsonUtil.usuariosToJson(
                                gestorUsuarios.listar()
                        )
                );

                return;
            }

            Usuario usuario =
                    gestorUsuarios.buscarPorId(id);

            if (usuario == null) {

                enviarJson(
                        exchange,
                        404,
                        JsonUtil.error(
                                "Usuario no encontrado"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.usuarioToJson(
                            usuario
                    )
            );
        }

        private void crearUsuario(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id != null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "POST debe realizarse sobre /usuarios"
                        )
                );

                return;
            }

            Map<String, String> datos =
                    JsonUtil.parseObject(
                            leerBody(exchange)
                    );

            Usuario usuario =
                    new Usuario(
                            JsonUtil.requerido(
                                    datos,
                                    "id"
                            ),
                            JsonUtil.requerido(
                                    datos,
                                    "nombre"
                            ),
                            JsonUtil.requerido(
                                    datos,
                                    "carrera"
                            )
                    );

            if (!gestorUsuarios.crear(
                    usuario)) {

                enviarJson(
                        exchange,
                        409,
                        JsonUtil.error(
                                "Ya existe un usuario con ese ID"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    201,
                    JsonUtil.usuarioToJson(
                            usuario
                    )
            );
        }

        private void actualizarUsuario(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "Debe indicar el ID: /usuarios/{id}"
                        )
                );

                return;
            }

            Map<String, String> datos =
                    JsonUtil.parseObject(
                            leerBody(exchange)
                    );

            boolean actualizado =
                    gestorUsuarios.actualizar(
                            id,
                            JsonUtil.requerido(
                                    datos,
                                    "nombre"
                            ),
                            JsonUtil.requerido(
                                    datos,
                                    "carrera"
                            )
                    );

            if (!actualizado) {

                enviarJson(
                        exchange,
                        404,
                        JsonUtil.error(
                                "Usuario no encontrado"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.usuarioToJson(
                            gestorUsuarios
                                    .buscarPorId(id)
                    )
            );
        }

        private void eliminarUsuario(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "Debe indicar el ID: /usuarios/{id}"
                        )
                );

                return;
            }

            if (gestorUsuarios
                    .buscarPorId(id) == null) {

                enviarJson(
                        exchange,
                        404,
                        JsonUtil.error(
                                "Usuario no encontrado"
                        )
                );

                return;
            }

            if (gestorApuntes
                    .existePorAutor(id)
                    ||
                    gestorValoraciones
                            .existePorUsuario(id)) {

                enviarJson(
                        exchange,
                        409,
                        JsonUtil.error(
                                "No se puede eliminar: "
                                + "el usuario tiene apuntes "
                                + "o valoraciones asociados"
                        )
                );

                return;
            }

            gestorUsuarios.eliminar(id);

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.mensaje(
                            "Usuario eliminado correctamente"
                    )
            );
        }
    }

    // ============================
    // APUNTES
    // ============================

    private class ApuntesHandler
            implements HttpHandler {

        @Override
        public void handle(
                HttpExchange exchange)
                throws IOException {

            try {

                String metodo =
                        exchange.getRequestMethod();

                String id =
                        obtenerId(
                                exchange,
                                "apuntes"
                        );

                switch (metodo) {

                    case "GET" ->
                            obtenerApuntes(
                                    exchange,
                                    id
                            );

                    case "POST" ->
                            crearApunte(
                                    exchange,
                                    id
                            );

                    case "PUT" ->
                            actualizarApunte(
                                    exchange,
                                    id
                            );

                    case "DELETE" ->
                            eliminarApunte(
                                    exchange,
                                    id
                            );

                    default ->
                            enviarJson(
                                    exchange,
                                    405,
                                    JsonUtil.error(
                                            "Metodo HTTP no permitido"
                                    )
                            );
                }

            } catch (
                    IllegalArgumentException e) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                e.getMessage()
                        )
                );

            } catch (Exception e) {

                enviarJson(
                        exchange,
                        500,
                        JsonUtil.error(
                                "Error interno del servidor"
                        )
                );
            }
        }

        private void obtenerApuntes(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id != null) {

                Apunte apunte =
                        gestorApuntes
                                .buscarPorId(id);

                if (apunte == null) {

                    enviarJson(
                            exchange,
                            404,
                            JsonUtil.error(
                                    "Apunte no encontrado"
                            )
                    );

                    return;
                }

                enviarJson(
                        exchange,
                        200,
                        JsonUtil.apunteToJson(
                                apunte
                        )
                );

                return;
            }

            Map<String, String> query =
                    obtenerQueryParams(
                            exchange
                    );

            String materia =
                    query.get("materia");

            if (materia != null
                    && !materia.isBlank()) {

                enviarJson(
                        exchange,
                        200,
                        JsonUtil.apuntesToJson(
                                gestorApuntes
                                        .buscarPorMateria(
                                                materia
                                        )
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.apuntesToJson(
                            gestorApuntes.listar()
                    )
            );
        }

        private void crearApunte(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id != null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "POST debe realizarse sobre /apuntes"
                        )
                );

                return;
            }

            Map<String, String> datos =
                    JsonUtil.parseObject(
                            leerBody(exchange)
                    );

            String autorId =
                    JsonUtil.requerido(
                            datos,
                            "autorId"
                    );

            Usuario autor =
                    gestorUsuarios
                            .buscarPorId(
                                    autorId
                            );

            if (autor == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "El autorId no corresponde "
                                + "a un usuario existente"
                        )
                );

                return;
            }

            Apunte apunte =
                    new Apunte(
                            JsonUtil.requerido(
                                    datos,
                                    "id"
                            ),
                            JsonUtil.requerido(
                                    datos,
                                    "titulo"
                            ),
                            JsonUtil.requerido(
                                    datos,
                                    "materia"
                            ),
                            JsonUtil.requerido(
                                    datos,
                                    "contenidoUrl"
                            ),
                            autor
                    );

            if (!gestorApuntes
                    .publicarApunte(
                            apunte
                    )) {

                enviarJson(
                        exchange,
                        409,
                        JsonUtil.error(
                                "Ya existe un apunte con ese ID"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    201,
                    JsonUtil.apunteToJson(
                            apunte
                    )
            );
        }

        private void actualizarApunte(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "Debe indicar el ID: /apuntes/{id}"
                        )
                );

                return;
            }

            Map<String, String> datos =
                    JsonUtil.parseObject(
                            leerBody(exchange)
                    );

            Usuario autor =
                    gestorUsuarios
                            .buscarPorId(
                                    JsonUtil.requerido(
                                            datos,
                                            "autorId"
                                    )
                            );

            if (autor == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "El autorId no corresponde "
                                + "a un usuario existente"
                        )
                );

                return;
            }

            boolean actualizado =
                    gestorApuntes.actualizar(
                            id,
                            JsonUtil.requerido(
                                    datos,
                                    "titulo"
                            ),
                            JsonUtil.requerido(
                                    datos,
                                    "materia"
                            ),
                            JsonUtil.requerido(
                                    datos,
                                    "contenidoUrl"
                            ),
                            autor
                    );

            if (!actualizado) {

                enviarJson(
                        exchange,
                        404,
                        JsonUtil.error(
                                "Apunte no encontrado"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.apunteToJson(
                            gestorApuntes
                                    .buscarPorId(id)
                    )
            );
        }

        private void eliminarApunte(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "Debe indicar el ID: /apuntes/{id}"
                        )
                );

                return;
            }

            if (gestorApuntes
                    .buscarPorId(id) == null) {

                enviarJson(
                        exchange,
                        404,
                        JsonUtil.error(
                                "Apunte no encontrado"
                        )
                );

                return;
            }

            if (gestorValoraciones
                    .existePorApunte(id)) {

                enviarJson(
                        exchange,
                        409,
                        JsonUtil.error(
                                "No se puede eliminar: "
                                + "el apunte tiene "
                                + "valoraciones asociadas"
                        )
                );

                return;
            }

            gestorApuntes.eliminar(id);

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.mensaje(
                            "Apunte eliminado correctamente"
                    )
            );
        }
    }

    // ============================
    // VALORACIONES
    // ============================

    private class ValoracionesHandler
            implements HttpHandler {

        @Override
        public void handle(
                HttpExchange exchange)
                throws IOException {

            try {

                String metodo =
                        exchange.getRequestMethod();

                String id =
                        obtenerId(
                                exchange,
                                "valoraciones"
                        );

                switch (metodo) {

                    case "GET" ->
                            obtenerValoraciones(
                                    exchange,
                                    id
                            );

                    case "POST" ->
                            crearValoracion(
                                    exchange,
                                    id
                            );

                    case "PUT" ->
                            actualizarValoracion(
                                    exchange,
                                    id
                            );

                    case "DELETE" ->
                            eliminarValoracion(
                                    exchange,
                                    id
                            );

                    default ->
                            enviarJson(
                                    exchange,
                                    405,
                                    JsonUtil.error(
                                            "Metodo HTTP no permitido"
                                    )
                            );
                }

            } catch (
                    IllegalArgumentException e) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                e.getMessage()
                        )
                );

            } catch (Exception e) {

                enviarJson(
                        exchange,
                        500,
                        JsonUtil.error(
                                "Error interno del servidor"
                        )
                );
            }
        }

        private void obtenerValoraciones(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id == null) {

                enviarJson(
                        exchange,
                        200,
                        JsonUtil.valoracionesToJson(
                                gestorValoraciones
                                        .listar()
                        )
                );

                return;
            }

            Valoracion valoracion =
                    gestorValoraciones
                            .buscarPorId(id);

            if (valoracion == null) {

                enviarJson(
                        exchange,
                        404,
                        JsonUtil.error(
                                "Valoracion no encontrada"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.valoracionToJson(
                            valoracion
                    )
            );
        }

        private void crearValoracion(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id != null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "POST debe realizarse "
                                + "sobre /valoraciones"
                        )
                );

                return;
            }

            Map<String, String> datos =
                    JsonUtil.parseObject(
                            leerBody(exchange)
                    );

            Usuario usuario =
                    gestorUsuarios
                            .buscarPorId(
                                    JsonUtil.requerido(
                                            datos,
                                            "usuarioId"
                                    )
                            );

            Apunte apunte =
                    gestorApuntes
                            .buscarPorId(
                                    JsonUtil.requerido(
                                            datos,
                                            "apunteId"
                                    )
                            );

            if (usuario == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "El usuarioId no corresponde "
                                + "a un usuario existente"
                        )
                );

                return;
            }

            if (apunte == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "El apunteId no corresponde "
                                + "a un apunte existente"
                        )
                );

                return;
            }

            Valoracion valoracion =
                    new Valoracion(
                            JsonUtil.requerido(
                                    datos,
                                    "id"
                            ),
                            usuario,
                            apunte,
                            JsonUtil.enteroRequerido(
                                    datos,
                                    "estrellas"
                            )
                    );

            if (!gestorValoraciones
                    .crear(valoracion)) {

                enviarJson(
                        exchange,
                        409,
                        JsonUtil.error(
                                "Ya existe una valoracion "
                                + "con ese ID"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    201,
                    JsonUtil.valoracionToJson(
                            valoracion
                    )
            );
        }

        private void actualizarValoracion(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "Debe indicar el ID: "
                                + "/valoraciones/{id}"
                        )
                );

                return;
            }

            Map<String, String> datos =
                    JsonUtil.parseObject(
                            leerBody(exchange)
                    );

            boolean actualizada =
                    gestorValoraciones
                            .actualizar(
                                    id,
                                    JsonUtil
                                            .enteroRequerido(
                                                    datos,
                                                    "estrellas"
                                            )
                            );

            if (!actualizada) {

                enviarJson(
                        exchange,
                        404,
                        JsonUtil.error(
                                "Valoracion no encontrada"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.valoracionToJson(
                            gestorValoraciones
                                    .buscarPorId(id)
                    )
            );
        }

        private void eliminarValoracion(
                HttpExchange exchange,
                String id)
                throws IOException {

            if (id == null) {

                enviarJson(
                        exchange,
                        400,
                        JsonUtil.error(
                                "Debe indicar el ID: "
                                + "/valoraciones/{id}"
                        )
                );

                return;
            }

            if (!gestorValoraciones
                    .eliminar(id)) {

                enviarJson(
                        exchange,
                        404,
                        JsonUtil.error(
                                "Valoracion no encontrada"
                        )
                );

                return;
            }

            enviarJson(
                    exchange,
                    200,
                    JsonUtil.mensaje(
                            "Valoracion eliminada correctamente"
                    )
            );
        }
    }

    // ============================
    // METODOS AUXILIARES
    // ============================

    private String obtenerId(
            HttpExchange exchange,
            String recurso) {

        String path =
                exchange
                        .getRequestURI()
                        .getPath();

        String base =
                "/" + recurso;

        if (path.equals(base)
                || path.equals(base + "/")) {

            return null;
        }

        if (path.startsWith(
                base + "/")) {

            String resto =
                    path.substring(
                            (base + "/").length()
                    );

            if (!resto.isBlank()
                    && !resto.contains("/")) {

                return decodificar(resto);
            }
        }

        throw new IllegalArgumentException(
                "Ruta no valida"
        );
    }

    private Map<String, String>
    obtenerQueryParams(
            HttpExchange exchange) {

        Map<String, String> params =
                new HashMap<>();

        String query =
                exchange
                        .getRequestURI()
                        .getRawQuery();

        if (query == null
                || query.isBlank()) {

            return params;
        }

        for (String par :
                query.split("&")) {

            String[] partes =
                    par.split("=", 2);

            String clave =
                    decodificar(
                            partes[0]
                    );

            String valor =
                    partes.length > 1
                            ? decodificar(
                            partes[1])
                            : "";

            params.put(
                    clave,
                    valor
            );
        }

        return params;
    }

    private String decodificar(
            String valor) {

        return URLDecoder.decode(
                valor,
                StandardCharsets.UTF_8
        );
    }

    private String leerBody(
            HttpExchange exchange)
            throws IOException {

        return new String(
                exchange
                        .getRequestBody()
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    private void enviarJson(
            HttpExchange exchange,
            int status,
            String json)
            throws IOException {

        byte[] respuesta =
                json.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange
                .getResponseHeaders()
                .set(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );

        exchange.sendResponseHeaders(
                status,
                respuesta.length
        );

        exchange
                .getResponseBody()
                .write(respuesta);

        exchange.close();
    }
}