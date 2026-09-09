package com.universidad.apuntes;

import com.universidad.apuntes.api.ApiServer;
import com.universidad.apuntes.model.Apunte;
import com.universidad.apuntes.model.Usuario;
import com.universidad.apuntes.model.Valoracion;
import com.universidad.apuntes.service.GestorApuntes;
import com.universidad.apuntes.service.GestorUsuarios;
import com.universidad.apuntes.service.GestorValoraciones;

public class Main {

    public static void main(String[] args) {

        try {

            GestorUsuarios gestorUsuarios =
                    new GestorUsuarios();

            GestorApuntes gestorApuntes =
                    new GestorApuntes();

            GestorValoraciones gestorValoraciones =
                    new GestorValoraciones();

            cargarDatosIniciales(
                    gestorUsuarios,
                    gestorApuntes,
                    gestorValoraciones
            );

            int puerto =
                    args.length > 0
                            ? Integer.parseInt(args[0])
                            : 8080;

            ApiServer apiServer =
                    new ApiServer(
                            puerto,
                            gestorUsuarios,
                            gestorApuntes,
                            gestorValoraciones
                    );

            apiServer.iniciar();

            System.out.println(
                    "==============================================="
            );

            System.out.println(
                    " Plataforma de Apuntes - API iniciada"
            );

            System.out.println(
                    " http://localhost:" + puerto
            );

            System.out.println(
                    "==============================================="
            );

            System.out.println(
                    "GET/POST       /usuarios"
            );

            System.out.println(
                    "GET/PUT/DELETE /usuarios/{id}"
            );

            System.out.println(
                    "GET/POST       /apuntes"
            );

            System.out.println(
                    "GET/PUT/DELETE /apuntes/{id}"
            );

            System.out.println(
                    "GET /apuntes?materia=Programacion%20II"
            );

            System.out.println(
                    "GET/POST       /valoraciones"
            );

            System.out.println(
                    "GET/PUT/DELETE /valoraciones/{id}"
            );

            System.out.println(
                    "==============================================="
            );

            System.out.println(
                    "Presiona Ctrl + C para detener el servidor."
            );

        } catch (Exception e) {

            System.err.println(
                    "No fue posible iniciar la aplicacion: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private static void cargarDatosIniciales(
            GestorUsuarios gestorUsuarios,
            GestorApuntes gestorApuntes,
            GestorValoraciones gestorValoraciones) {

        Usuario u1 =
                new Usuario(
                        "001",
                        "Carlos Pena",
                        "Sistemas"
                );

        Usuario u2 =
                new Usuario(
                        "002",
                        "Valeria Ordonez",
                        "Software"
                );

        gestorUsuarios.crear(u1);
        gestorUsuarios.crear(u2);

        Apunte a1 =
                new Apunte(
                        "A01",
                        "Guia de POO",
                        "Programacion II",
                        "https://drive.google.com/guia-poo",
                        u1
                );

        Apunte a2 =
                new Apunte(
                        "A02",
                        "Normalizacion BD",
                        "Bases de Datos",
                        "https://drive.google.com/normalizacion-bd",
                        u2
                );

        gestorApuntes.publicarApunte(a1);
        gestorApuntes.publicarApunte(a2);

        Valoracion v1 =
                new Valoracion(
                        "V01",
                        u2,
                        a1,
                        5
                );

        gestorValoraciones.crear(v1);
    }
}