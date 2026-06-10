package com.tomas.cshub.model;

import com.tomas.cshub.model.base.EntidadAuditable;
import com.tomas.cshub.model.enums.Nivel;
import com.tomas.cshub.model.enums.TemaCS;
import com.tomas.cshub.model.enums.TipoRecurso;
import jakarta.persistence.*;

@Entity
@Table(name = "recursos")
public class Recurso extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // nullable = true → la URL es opcional si se sube un archivo
    @Column(nullable = true)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRecurso tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TemaCS tema;

    // Nombre original del archivo subido (para mostrar al usuario)
    @Column(name = "archivo_nombre")
    private String archivoNombre;

    // Nombre único del archivo en el servidor (para encontrarlo en disco)
   @Column(name = "archivo_ruta")
   private String archivoRuta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nivel nivel;

    private String idioma;

    private String fuente;

    private Boolean gratuito;

    private Boolean esUdeA;


    //  Constructor vacío
    public Recurso() {}

    //  Constructor con datos 
    public Recurso(String titulo, String descripcion, String url,
                   TipoRecurso tipo, TemaCS tema, Nivel nivel,
                   String idioma, String fuente, Boolean gratuito) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.tipo = tipo;
        this.tema = tema;
        this.nivel = nivel;
        this.idioma = idioma;
        this.fuente = fuente;
        this.gratuito = gratuito;
    }

    public String getTipoNombre() {
        return tipo != null ? tipo.getNombre() : "";
    }

    public String getTemaNombre() {
        return tema != null ? tema.getNombre() : "";
    }

    public String getNivelNombre() {
        return nivel != null ? nivel.getNombre() : "";
    }

    //  Getters y Setters 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public TipoRecurso getTipo() { return tipo; }
    public void setTipo(TipoRecurso tipo) { this.tipo = tipo; }

    public TemaCS getTema() { return tema; }
    public void setTema(TemaCS tema) { this.tema = tema; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }

    public Boolean getGratuito() { return gratuito; }
    public void setGratuito(Boolean gratuito) { this.gratuito = gratuito; }

    public Boolean getEsUdeA() { return esUdeA; }
    public void setEsUdeA(Boolean esUdeA) { this.esUdeA = esUdeA; }

    public String getArchivoNombre() { return archivoNombre; }
public void setArchivoNombre(String archivoNombre) {
    this.archivoNombre = archivoNombre;
}

public String getArchivoRuta() { return archivoRuta; }
public void setArchivoRuta(String archivoRuta) {
    this.archivoRuta = archivoRuta;
}

// Método útil: devuelve true si este recurso tiene archivo adjunto
public boolean tieneArchivo() {
    return archivoRuta != null && !archivoRuta.isEmpty();
}
}