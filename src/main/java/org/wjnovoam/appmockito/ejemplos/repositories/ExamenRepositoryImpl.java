package org.wjnovoam.appmockito.ejemplos.repositories;

import org.wjnovoam.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository{

    @Override
    public List<Examen> findAll() {
        return Arrays.asList(new Examen(5L, "Matematicas"),
                new Examen(5L, "Lenguaje"),
                new Examen(5L, "Historial"),
                new Examen(5L, "Español"));
    }
}
