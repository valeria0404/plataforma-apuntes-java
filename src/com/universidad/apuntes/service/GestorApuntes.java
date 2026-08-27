package com.universidad.apuntes.service;

import com.universidad.apuntes.model.Apunte;
import java.util.ArrayList;
import java.util.List;

public class GestorApuntes {
    private List<Apunte> catalogo;

    public GestorApuntes() {
        this.catalogo = new ArrayList<>();
    }

    public void publicarApunte(Apunte apunte) {
        catalogo.add(apunte);
        System.out.println("✅ Apunte publicado: " + apunte.getTitulo());
    }

    public List<Apunte> buscarPorMateria(String materia) {
        List<Apunte> resultados = new ArrayList<>();
        for (Apunte a : catalogo) {
            if (a.getMateria().equalsIgnoreCase(materia)) {
                resultados.add(a);
            }
        }
        return resultados;
    }
}