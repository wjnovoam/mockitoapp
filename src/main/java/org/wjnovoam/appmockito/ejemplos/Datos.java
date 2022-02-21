package org.wjnovoam.appmockito.ejemplos;

import org.wjnovoam.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Matematicas"),
            new Examen(5L, "Lenguaje"), new Examen(2L, "Historial"),
            new Examen(1L, "Español"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Matematicas"),
            new Examen(null, "Lenguaje"), new Examen(null, "Historial"),
            new Examen(null, "Español"));

    public final static List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(new Examen(-5L, "Matematicas"),
            new Examen(-5L, "Lenguaje"), new Examen(-2L, "Historial"),
            new Examen(-1L, "Español"));

    public final static List<String> PREGUNTAS = Arrays.asList("aritmetica", "integrales","derivadas",
            "trigonometria", "geometria");

    public final static Examen EXAMEN = new Examen(null, "Fisica");
}
