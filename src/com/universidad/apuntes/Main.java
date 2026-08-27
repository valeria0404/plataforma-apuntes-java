package com.universidad.apuntes;

import com.universidad.apuntes.model.Apunte;
import com.universidad.apuntes.model.Usuario;
import com.universidad.apuntes.service.GestorApuntes;

public class Main {
    public static void main(String[] args) {
        GestorApuntes gestor = new GestorApuntes();

        Usuario u1 = new Usuario("001", "Carlos Peña", "Sistemas");
        Usuario u2 = new Usuario("002", "Valeria Ordóñez", "Software");

        Apunte a1 = new Apunte("A01", "Guía de POO", "Programación II", "https://drive.google.com/...", u1);
        Apunte a2 = new Apunte("A02", "Normalización BD", "Bases de Datos", "https://drive.google.com/...", u2);

        gestor.publicarApunte(a1);
        gestor.publicarApunte(a2);

        a1.valorar();

        System.out.println("\n--- Búsqueda de Apuntes (Programación II) ---");
        for (Apunte apunte : gestor.buscarPorMateria("Programación II")) {
            System.out.println(apunte);
        }
    }
}