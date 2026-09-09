package com.universidad.apuntes.api;

import com.universidad.apuntes.model.Apunte;
import com.universidad.apuntes.model.Usuario;
import com.universidad.apuntes.model.Valoracion;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonUtil {

    private static final Pattern CAMPO_JSON = Pattern.compile(
            "\\\"([^\\\"]+)\\\"\\s*:\\s*(\\\"(?:\\\\.|[^\\\"])*\\\"|-?\\d+(?:\\.\\d+)?|true|false|null)"
    );

    private JsonUtil() {
    }

    public static Map<String, String> parseObject(String json) {

        Map<String, String> datos = new LinkedHashMap<>();

        if (json == null || json.isBlank()) {
            return datos;
        }

        Matcher matcher = CAMPO_JSON.matcher(json);

        while (matcher.find()) {

            String clave = matcher.group(1);
            String valor = matcher.group(2);

            if (valor.startsWith("\"")
                    && valor.endsWith("\"")) {

                valor = valor.substring(
                        1,
                        valor.length() - 1
                );

                valor = valor
                        .replace("\\\"", "\"")
                        .replace("\\n", "\n")
                        .replace("\\r", "\r")
                        .replace("\\t", "\t")
                        .replace("\\\\", "\\");
            }

            datos.put(
                    clave,
                    "null".equals(valor)
                            ? null
                            : valor
            );
        }

        return datos;
    }

    public static String requerido(
            Map<String, String> datos,
            String clave) {

        String valor = datos.get(clave);

        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "El campo '" + clave + "' es obligatorio"
            );
        }

        return valor;
    }

    public static int enteroRequerido(
            Map<String, String> datos,
            String clave) {

        String valor = requerido(datos, clave);

        try {

            return Integer.parseInt(valor);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "El campo '" + clave
                            + "' debe ser un numero entero"
            );
        }
    }

    public static String usuarioToJson(
            Usuario usuario) {

        return "{"
                + "\"id\":" + quote(usuario.getId()) + ","
                + "\"nombre\":" + quote(usuario.getNombre()) + ","
                + "\"carrera\":" + quote(usuario.getCarrera())
                + "}";
    }

    public static String usuariosToJson(
            List<Usuario> usuarios) {

        StringBuilder json =
                new StringBuilder("[");

        for (int i = 0; i < usuarios.size(); i++) {

            if (i > 0) {
                json.append(",");
            }

            json.append(
                    usuarioToJson(
                            usuarios.get(i)
                    )
            );
        }

        return json.append("]").toString();
    }

    public static String apunteToJson(
            Apunte apunte) {

        return "{"
                + "\"id\":" + quote(apunte.getId()) + ","
                + "\"titulo\":" + quote(apunte.getTitulo()) + ","
                + "\"materia\":" + quote(apunte.getMateria()) + ","
                + "\"contenidoUrl\":" + quote(apunte.getContenidoUrl()) + ","
                + "\"autor\":" + usuarioToJson(apunte.getAutor()) + ","
                + "\"valoraciones\":" + apunte.getValoraciones()
                + "}";
    }

    public static String apuntesToJson(
            List<Apunte> apuntes) {

        StringBuilder json =
                new StringBuilder("[");

        for (int i = 0; i < apuntes.size(); i++) {

            if (i > 0) {
                json.append(",");
            }

            json.append(
                    apunteToJson(
                            apuntes.get(i)
                    )
            );
        }

        return json.append("]").toString();
    }

    public static String valoracionToJson(
            Valoracion valoracion) {

        return "{"
                + "\"id\":" + quote(valoracion.getId()) + ","
                + "\"usuarioId\":"
                + quote(valoracion.getUsuario().getId()) + ","
                + "\"apunteId\":"
                + quote(valoracion.getApunte().getId()) + ","
                + "\"estrellas\":"
                + valoracion.getEstrellas()
                + "}";
    }

    public static String valoracionesToJson(
            List<Valoracion> valoraciones) {

        StringBuilder json =
                new StringBuilder("[");

        for (int i = 0;
             i < valoraciones.size();
             i++) {

            if (i > 0) {
                json.append(",");
            }

            json.append(
                    valoracionToJson(
                            valoraciones.get(i)
                    )
            );
        }

        return json.append("]").toString();
    }

    public static String mensaje(
            String mensaje) {

        return "{\"mensaje\":"
                + quote(mensaje)
                + "}";
    }

    public static String error(
            String mensaje) {

        return "{\"error\":"
                + quote(mensaje)
                + "}";
    }

    public static String quote(
            String texto) {

        if (texto == null) {
            return "null";
        }

        return "\""
                + escape(texto)
                + "\"";
    }

    private static String escape(
            String texto) {

        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}