package com.tomas.cshub.service.impl;

import com.tomas.cshub.model.Recurso;
import com.tomas.cshub.model.Seguimiento;
import com.tomas.cshub.model.enums.EstadoSeguimiento;
import com.tomas.cshub.repository.SeguimientoRepository;
import com.tomas.cshub.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class SeguimientoServiceImpl implements SeguimientoService {

    @Autowired
    private SeguimientoRepository seguimientoRepository;

    @Autowired
    private com.tomas.cshub.repository.RecursoRepository recursoRepository;

    @Override
    public List<Seguimiento> listar() {
        return seguimientoRepository.findAllByOrderByFechaCreacionDesc();
    }

    @Override
    public Seguimiento guardar(Seguimiento seguimiento) {

        // Validación: nombre de usuario obligatorio
        if (seguimiento.getNombreUsuario() == null ||
                seguimiento.getNombreUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }

        // Validación: debe tener un recurso asociado
        if (seguimiento.getRecurso() == null) {
            throw new IllegalArgumentException("Debes seleccionar un recurso.");
        }

        // Validación: no puede seguir el mismo recurso dos veces
        if (seguimiento.getId() == null && // solo al crear, no al editar
            seguimientoRepository.existsByNombreUsuarioAndRecurso(
                seguimiento.getNombreUsuario(), seguimiento.getRecurso())) {
            throw new IllegalArgumentException(
                "Ya tienes este recurso en tu lista de seguimiento.");
        }

        // Si no se especifica fecha de inicio, se usa hoy
        if (seguimiento.getFechaInicio() == null) {
            seguimiento.setFechaInicio(LocalDate.now());
        }

        // Validación: fecha meta debe ser posterior a fecha de inicio
        if (seguimiento.getFechaMeta() != null &&
            seguimiento.getFechaMeta().isBefore(seguimiento.getFechaInicio())) {
            throw new IllegalArgumentException(
                "La fecha meta debe ser posterior a la fecha de inicio.");
        }

        // Si no se especifica estado, empieza como PENDIENTE
        if (seguimiento.getEstado() == null) {
            seguimiento.setEstado(EstadoSeguimiento.PENDIENTE);
        }

        return seguimientoRepository.save(seguimiento);
    }

    @Override
    public Seguimiento buscar(Long id) {
        return seguimientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Seguimiento no encontrado con ID: " + id));
    }

    @Override
    public void eliminar(Long id) {
        if (!seguimientoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                "No se puede eliminar: seguimiento no encontrado con ID: " + id);
        }
        seguimientoRepository.deleteById(id);
    }

    @Override
    public List<Seguimiento> buscarPorUsuario(String nombreUsuario) {
        return seguimientoRepository.findByNombreUsuario(nombreUsuario);
    }

    @Override
    public List<Seguimiento> buscarPorRecurso(Recurso recurso) {
        return seguimientoRepository.findByRecurso(recurso);
    }

    @Override
    public List<Seguimiento> buscarPorEstado(EstadoSeguimiento estado) {
        return seguimientoRepository.findByEstado(estado);
    }

    @Override
    public List<Seguimiento> buscar(String nombreUsuario, EstadoSeguimiento estado) {
        return seguimientoRepository.findByNombreUsuarioAndEstado(
                nombreUsuario, estado);
    }

    // Operación especial: cambia el estado de un seguimiento
    @Override
    public Seguimiento cambiarEstado(Long id, EstadoSeguimiento nuevoEstado) {
        Seguimiento seguimiento = buscar(id);
        seguimiento.setEstado(nuevoEstado);
        return seguimientoRepository.save(seguimiento);
    }
}