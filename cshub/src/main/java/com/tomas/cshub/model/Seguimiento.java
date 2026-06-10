package com.tomas.cshub.model;

import com.tomas.cshub.model.base.EntidadAuditable;
import com.tomas.cshub.model.enums.EstadoSeguimiento;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seguimientos")
public class Seguimiento extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de quien hace el seguimiento
    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = false)
    private Recurso recurso;

    // Fecha en que el usuario empieza el seguimiento
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    // Fecha meta para terminar el recurso
    @Column(name = "fecha_meta")
    private LocalDate fechaMeta;

    // Estado actual: PENDIENTE, EN_PROGRESO, COMPLETADO
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSeguimiento estado;

    // Notas personales del usuario sobre el recurso
    @Column(columnDefinition = "TEXT")
    private String notas;

    //  Constructor vacío 
    public Seguimiento() {}

    //  Constructor con datos 
    public Seguimiento(String nombreUsuario, Recurso recurso,
                       LocalDate fechaInicio, LocalDate fechaMeta,
                       EstadoSeguimiento estado, String notas) {
        this.nombreUsuario = nombreUsuario;
        this.recurso = recurso;
        this.fechaInicio = fechaInicio;
        this.fechaMeta = fechaMeta;
        this.estado = estado;
        this.notas = notas;
    }

    //  Método útil 
    public String getEstadoNombre() {
        return estado != null ? estado.getNombre() : "";
    }

    //  Getters y Setters 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Recurso getRecurso() { return recurso; }
    public void setRecurso(Recurso recurso) { this.recurso = recurso; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaMeta() { return fechaMeta; }
    public void setFechaMeta(LocalDate fechaMeta) {
        this.fechaMeta = fechaMeta;
    }

    public EstadoSeguimiento getEstado() { return estado; }
    public void setEstado(EstadoSeguimiento estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}