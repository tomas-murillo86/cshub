package com.tomas.cshub.model.enums;

public enum Nivel {
    BASICO("Básico"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado");

    private final String nombre;

    Nivel(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}