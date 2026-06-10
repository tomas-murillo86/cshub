package com.tomas.cshub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Lee el valor de app.upload.dir del application.properties
    @Value("${app.upload.dir}")
    private String uploadDir;

    // Guarda el archivo y devuelve el nombre único generado
    public String guardarArchivo(MultipartFile archivo) throws IOException {

        // Crea la carpeta uploads si no existe
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Genera un nombre único para evitar colisiones
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal.substring(
                nombreOriginal.lastIndexOf("."));
        String nombreUnico = UUID.randomUUID().toString() + extension;

        // Copia el archivo a la carpeta uploads
        Path destino = uploadPath.resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), destino,
                StandardCopyOption.REPLACE_EXISTING);

        return nombreUnico;
    }

    // Elimina un archivo del servidor
    public void eliminarArchivo(String nombreArchivo) throws IOException {
        Path archivo = Paths.get(uploadDir).resolve(nombreArchivo);
        Files.deleteIfExists(archivo);
    }

    // Devuelve la ruta completa de un archivo para descargarlo
    public Path obtenerRutaArchivo(String nombreArchivo) {
        return Paths.get(uploadDir).resolve(nombreArchivo);
    }

    // Valida que el archivo sea PDF, DOCX o PPTX
    public boolean esFormatoPermitido(MultipartFile archivo) {
        String nombre = archivo.getOriginalFilename();
        if (nombre == null) return false;
        String ext = nombre.toLowerCase();
        return ext.endsWith(".pdf") ||
               ext.endsWith(".docx") ||
               ext.endsWith(".pptx");
    }
}