package org.wjnovoam.appmockito.ejemplos.service;

import org.wjnovoam.appmockito.ejemplos.models.Examen;
import org.wjnovoam.appmockito.ejemplos.repositories.ExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{

    private ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Examen findExamenPorNombre(String nombre) throws Exception {
        Optional<Examen> examenOptional = examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();

        Examen examen = null;
        if(examenOptional.isPresent()){
            examen = examenOptional.orElseThrow(() -> new Exception("examen not found"));
        }
        return examen;
    }
}
