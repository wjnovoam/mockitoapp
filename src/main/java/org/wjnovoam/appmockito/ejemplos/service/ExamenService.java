package org.wjnovoam.appmockito.ejemplos.service;

import org.wjnovoam.appmockito.ejemplos.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenPorNombre(String nombre);
    Examen findExamenPorNombreConPreguntas(String nombre);

    Examen guardar(Examen examen);
}
