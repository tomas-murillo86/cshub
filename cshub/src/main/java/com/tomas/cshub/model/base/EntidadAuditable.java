package com.tomas.cshub.model.base;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class EntidadAuditable {

    // Se asigna automáticamente cuando se crea el registro
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // Se actualiza automáticamente cada vez que se modifica el registro
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // @PrePersist
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // @PreUpdate
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}