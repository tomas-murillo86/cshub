package com.tomas.cshub.service.impl;

import com.tomas.cshub.model.Recurso;
import com.tomas.cshub.model.enums.Nivel;
import com.tomas.cshub.model.enums.TemaCS;
import com.tomas.cshub.model.enums.TipoRecurso;
import com.tomas.cshub.repository.RecursoRepository;
import com.tomas.cshub.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecursoServiceImpl implements RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    // CRUD básico 

    @Override
    public List<Recurso> listar() {
        return recursoRepository.findAll();
    }

    @Override
    public Recurso guardar(Recurso recurso) {
        // Validaciones de negocio antes de guardar
        if (recurso.getTitulo() == null || recurso.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del recurso es obligatorio.");
        }
        // Validación: debe tener URL o archivo, no puede no tener ninguno
        boolean tieneUrl = recurso.getUrl() != null && !recurso.getUrl().trim().isEmpty();
        boolean tieneArchivo = recurso.getArchivoRuta() != null && !recurso.getArchivoRuta().isEmpty();
        if (!tieneUrl && !tieneArchivo) {
            throw new IllegalArgumentException(
                "Debes proporcionar una URL o subir un archivo.");
        }
        if (recurso.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de recurso es obligatorio.");
        }
        if (recurso.getTema() == null) {
            throw new IllegalArgumentException("El tema del recurso es obligatorio.");
        }
        if (recurso.getNivel() == null) {
            throw new IllegalArgumentException("El nivel del recurso es obligatorio.");
        }
        // Si no se especifica si es gratuito, se asume que sí
        if (recurso.getGratuito() == null) {
            recurso.setGratuito(true);
        }
        return recursoRepository.save(recurso);
    }

    @Override
    public Recurso buscar(Long id) {

        return recursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recurso no encontrado con ID: " + id));
    }

    @Override
    public void eliminar(Long id) {
        if (!recursoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar: recurso no encontrado con ID: " + id);
        }
        recursoRepository.deleteById(id);
    }


    @Override
    public List<Recurso> buscar(TipoRecurso tipo) {
        return recursoRepository.findByTipo(tipo);
    }

    @Override
    public List<Recurso> buscar(TemaCS tema) {
        return recursoRepository.findByTema(tema);
    }

    @Override
    public List<Recurso> buscar(Nivel nivel) {
        return recursoRepository.findByNivel(nivel);
    }

    @Override
    public List<Recurso> buscar(TemaCS tema, Nivel nivel) {
        return recursoRepository.findByTemaAndNivel(tema, nivel);
    }

    @Override
    public List<Recurso> buscar(TipoRecurso tipo, TemaCS tema) {
        return recursoRepository.findByTipoAndTema(tipo, tema);
    }

    // ── Operaciones adicionales 

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        return recursoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    public List<Recurso> listarGratuitos() {
        return recursoRepository.findByGratuito(true);
    }

    @Override
    public List<Recurso> listarUdeA() {
        return recursoRepository.findByEsUdeA(true);
    }

    @Override
    public List<Recurso> listarRecientes() {
        return recursoRepository.findAllByOrderByFechaCreacionDesc();
    }
}