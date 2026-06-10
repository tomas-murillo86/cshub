package com.tomas.cshub.model.enums;

public enum TipoRecurso {
    CURSO("Curso"),
    VIDEO("Video"),
    LIBRO("Libro"),
    GUIA("Guía"),
    ROADMAP("Roadmap");

    // Cada valor del enum tiene un nombre legible en español
    private final String nombre;

    TipoRecurso(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}