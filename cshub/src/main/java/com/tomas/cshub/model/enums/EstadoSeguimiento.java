package com.tomas.cshub.model.enums;

public enum EstadoSeguimiento {
    PENDIENTE("Pendiente"),
    EN_PROGRESO("En Progreso"),
    COMPLETADO("Completado");

    private final String nombre;

    EstadoSeguimiento(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}