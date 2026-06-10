package com.tomas.cshub.service;

import com.tomas.cshub.model.Recurso;
import com.tomas.cshub.model.Seguimiento;
import com.tomas.cshub.model.enums.EstadoSeguimiento;
import java.util.List;

public interface SeguimientoService {

    //  CRUD básico 
    List<Seguimiento> listar();
    Seguimiento guardar(Seguimiento seguimiento);
    Seguimiento buscar(Long id);
    void eliminar(Long id);

    //  Búsquedas específicas 
    List<Seguimiento> buscarPorUsuario(String nombreUsuario);
    List<Seguimiento> buscarPorRecurso(Recurso recurso);
    List<Seguimiento> buscarPorEstado(EstadoSeguimiento estado);

    //  Sobrecarga 
    List<Seguimiento> buscar(String nombreUsuario, EstadoSeguimiento estado);

    //  Operación especial: cambiar estado 
    // Permite pasar de PENDIENTE → EN_PROGRESO → COMPLETADO
    Seguimiento cambiarEstado(Long id, EstadoSeguimiento nuevoEstado);
}