package com.tomas.cshub.controller;

import com.tomas.cshub.model.Recurso;
import com.tomas.cshub.model.enums.Nivel;
import com.tomas.cshub.model.enums.TemaCS;
import com.tomas.cshub.model.enums.TipoRecurso;
import com.tomas.cshub.service.FileStorageService;
import com.tomas.cshub.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.net.MalformedURLException;
import java.nio.file.Path;

@Controller
@RequestMapping("/recursos")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("recursos", recursoService.listarRecientes());
        model.addAttribute("tipos", TipoRecurso.values());
        model.addAttribute("temas", TemaCS.values());
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("titulo", "Catálogo de Recursos CS");
        return "recurso/lista";
    }

    @GetMapping("/filtrar")
    public String filtrar(@RequestParam(required = false) String tipo,
                          @RequestParam(required = false) String tema,
                          @RequestParam(required = false) String nivel,
                          Model model) {
        if (tipo != null && !tipo.isEmpty() && tema != null && !tema.isEmpty()) {
            model.addAttribute("recursos",
                recursoService.buscar(TipoRecurso.valueOf(tipo), TemaCS.valueOf(tema)));
        } else if (tema != null && !tema.isEmpty() && nivel != null && !nivel.isEmpty()) {
            model.addAttribute("recursos",
                recursoService.buscar(TemaCS.valueOf(tema), Nivel.valueOf(nivel)));
        } else if (tipo != null && !tipo.isEmpty()) {
            model.addAttribute("recursos",
                recursoService.buscar(TipoRecurso.valueOf(tipo)));
        } else if (tema != null && !tema.isEmpty()) {
            model.addAttribute("recursos",
                recursoService.buscar(TemaCS.valueOf(tema)));
        } else if (nivel != null && !nivel.isEmpty()) {
            model.addAttribute("recursos",
                recursoService.buscar(Nivel.valueOf(nivel)));
        } else {
            model.addAttribute("recursos", recursoService.listarRecientes());
        }
        model.addAttribute("tipos", TipoRecurso.values());
        model.addAttribute("temas", TemaCS.values());
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("titulo", "Recursos Filtrados");
        return "recurso/lista";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String q, Model model) {
        model.addAttribute("recursos", recursoService.buscarPorTitulo(q));
        model.addAttribute("tipos", TipoRecurso.values());
        model.addAttribute("temas", TemaCS.values());
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("titulo", "Resultados para: " + q);
        return "recurso/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("recurso", recursoService.buscar(id));
        model.addAttribute("titulo", "Detalle del Recurso");
        return "recurso/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("recurso", new Recurso());
        model.addAttribute("tipos", TipoRecurso.values());
        model.addAttribute("temas", TemaCS.values());
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("titulo", "Agregar Recurso");
        return "recurso/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("recurso", recursoService.buscar(id));
        model.addAttribute("tipos", TipoRecurso.values());
        model.addAttribute("temas", TemaCS.values());
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("titulo", "Editar Recurso");
        return "recurso/form";
    }

    // ── GUARDAR con archivo opcional ─────────────────────────────
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Recurso recurso,
                          // @RequestParam para recibir el archivo del formulario
                          @RequestParam(value = "archivo", required = false)
                          MultipartFile archivo,
                          RedirectAttributes redirect) {
        try {
            // Si se subió un archivo, procesarlo
            if (archivo != null && !archivo.isEmpty()) {

                // Validar formato
                if (!fileStorageService.esFormatoPermitido(archivo)) {
                    redirect.addFlashAttribute("error",
                        "Solo se permiten archivos PDF, DOCX o PPTX.");
                    return "redirect:/recursos/nuevo";
                }

                // Si ya tenía un archivo anterior, eliminarlo del servidor
                if (recurso.getArchivoRuta() != null) {
                    fileStorageService.eliminarArchivo(recurso.getArchivoRuta());
                }

                // Guardar el nuevo archivo
                String nombreUnico = fileStorageService.guardarArchivo(archivo);
                recurso.setArchivoNombre(archivo.getOriginalFilename());
                recurso.setArchivoRuta(nombreUnico);
            }

            recursoService.guardar(recurso);
            redirect.addFlashAttribute("exito", "Recurso guardado correctamente ✅");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/recursos/nuevo";
        }
        return "redirect:/recursos";
    }

    // ── DESCARGAR archivo ─────────────────────────────────────────
    // GET /recursos/descargar/1 → descarga el archivo del recurso 1
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargar(@PathVariable Long id) {
        try {
            Recurso recurso = recursoService.buscar(id);

            if (!recurso.tieneArchivo()) {
                return ResponseEntity.notFound().build();
            }

            Path ruta = fileStorageService.obtenerRutaArchivo(
                    recurso.getArchivoRuta());
            Resource resource = new UrlResource(ruta.toUri());

            // Fuerza la descarga en el navegador con el nombre original
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                            recurso.getArchivoNombre() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            Recurso recurso = recursoService.buscar(id);
            // Si tiene archivo, eliminarlo del servidor también
            if (recurso.tieneArchivo()) {
                fileStorageService.eliminarArchivo(recurso.getArchivoRuta());
            }
            recursoService.eliminar(id);
            redirect.addFlashAttribute("exito",
                    "Recurso eliminado correctamente ✅");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/recursos";
    }

    @GetMapping("/udea")
    public String udea(Model model) {
        model.addAttribute("recursos", recursoService.listarUdeA());
        model.addAttribute("tipos", TipoRecurso.values());
        model.addAttribute("temas", TemaCS.values());
        model.addAttribute("niveles", Nivel.values());
        model.addAttribute("titulo", "Recursos UdeA");
        return "recurso/lista";
    }
}