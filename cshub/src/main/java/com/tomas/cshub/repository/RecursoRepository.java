package com.tomas.cshub.repository;

import com.tomas.cshub.model.Recurso;
import com.tomas.cshub.model.enums.Nivel;
import com.tomas.cshub.model.enums.TemaCS;
import com.tomas.cshub.model.enums.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    //  SELECT * FROM recursos WHERE tipo = ?
    List<Recurso> findByTipo(TipoRecurso tipo);

    //  SELECT * FROM recursos WHERE tema = ?
    List<Recurso> findByTema(TemaCS tema);

    //  SELECT * FROM recursos WHERE nivel = ?
    List<Recurso> findByNivel(Nivel nivel);

    //  SELECT * FROM recursos WHERE tipo = ? AND tema = ?
    List<Recurso> findByTipoAndTema(TipoRecurso tipo, TemaCS tema);

    //  SELECT * FROM recursos WHERE gratuito = ?
    List<Recurso> findByGratuito(Boolean gratuito);

    List<Recurso> findByEsUdeA(Boolean esUdeA);

    //  SELECT * FROM recursos WHERE titulo LIKE %palabra%
    // Busca recursos cuyo título contenga el texto buscado
    // IgnoreCase no distingue mayúsculas de minúsculas
    List<Recurso> findByTituloContainingIgnoreCase(String titulo);

    //  SELECT * FROM recursos WHERE tema = ? AND nivel = ?
    List<Recurso> findByTemaAndNivel(TemaCS tema, Nivel nivel);

    //  SELECT * FROM recursos ORDER BY fecha_creacion DESC
    // Trae los recursos más recientes primero
    List<Recurso> findAllByOrderByFechaCreacionDesc();
}