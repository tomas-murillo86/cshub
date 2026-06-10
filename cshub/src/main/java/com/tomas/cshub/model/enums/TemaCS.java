package com.tomas.cshub.model.enums;

public enum TemaCS {
    ALGORITMOS("Algoritmos y Estructuras de Datos"),
    PROGRAMACION("Programación"),
    REDES("Redes y Sistemas"),
    INTELIGENCIA_ARTIFICIAL("Inteligencia Artificial"),
    BASES_DATOS("Bases de Datos"),
    DESARROLLO_WEB("Desarrollo Web"),
    SEGURIDAD("Ciberseguridad"),
    MATEMATICAS("Matemáticas para CS");

    private final String nombre;

    TemaCS(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}