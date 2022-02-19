package org.wjnovoam.appmockito.ejemplos.repositories;

import org.wjnovoam.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
}
