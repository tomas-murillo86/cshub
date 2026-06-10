package com.tomas.cshub.service;

import com.tomas.cshub.model.Recurso;
import com.tomas.cshub.model.enums.Nivel;
import com.tomas.cshub.model.enums.TemaCS;
import com.tomas.cshub.model.enums.TipoRecurso;
import java.util.List;

public interface RecursoService {
    //  Operaciones básicas CRUD 
    List<Recurso> listar();
    Recurso guardar(Recurso recurso);
    Recurso buscar(Long id);
    void eliminar(Long id);

    List<Recurso> buscar(TipoRecurso tipo);
    List<Recurso> buscar(TemaCS tema);
    List<Recurso> buscar(Nivel nivel);
    List<Recurso> buscar(TemaCS tema, Nivel nivel);
    List<Recurso> buscar(TipoRecurso tipo, TemaCS tema);

    List<Recurso> buscarPorTitulo(String titulo);
    List<Recurso> listarGratuitos();
    List<Recurso> listarUdeA();
    List<Recurso> listarRecientes();
}