package com.tomas.cshub.repository;

import com.tomas.cshub.model.Recurso;
import com.tomas.cshub.model.Seguimiento;
import com.tomas.cshub.model.enums.EstadoSeguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    //  Todos los seguimientos de un usuario específico
    List<Seguimiento> findByNombreUsuario(String nombreUsuario);

    //  Todos los seguimientos de un recurso específico
    List<Seguimiento> findByRecurso(Recurso recurso);

    //  Seguimientos filtrados por estado
    List<Seguimiento> findByEstado(EstadoSeguimiento estado);

    //  Seguimientos de un usuario filtrados por estado
    List<Seguimiento> findByNombreUsuarioAndEstado(
            String nombreUsuario, EstadoSeguimiento estado);

    //  Verifica si un usuario ya tiene seguimiento de un recurso
    boolean existsByNombreUsuarioAndRecurso(String nombreUsuario, Recurso recurso);

    //  Todos los seguimientos ordenados por fecha de creación
    List<Seguimiento> findAllByOrderByFechaCreacionDesc();
}