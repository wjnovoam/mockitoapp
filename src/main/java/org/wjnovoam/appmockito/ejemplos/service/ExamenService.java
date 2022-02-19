package org.wjnovoam.appmockito.ejemplos.service;

import org.wjnovoam.appmockito.ejemplos.models.Examen;

public interface ExamenService {
    Examen findExamenPorNombre(String nombre) throws Exception;
}
