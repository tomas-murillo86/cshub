package com.tomas.cshub.controller;

import com.tomas.cshub.model.Seguimiento;
import com.tomas.cshub.model.enums.EstadoSeguimiento;
import com.tomas.cshub.service.RecursoService;
import com.tomas.cshub.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/seguimientos")
public class SeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;

    @Autowired
    private RecursoService recursoService;

    //  LISTAR TODOS 
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("seguimientos", seguimientoService.listar());
        model.addAttribute("estados", EstadoSeguimiento.values());
        model.addAttribute("titulo", "Lista de Seguimientos");
        return "seguimiento/lista";
    }

    //  MI LISTA (filtrar por usuario) 
    @GetMapping("/usuario")
    public String miLista(@RequestParam String nombre, Model model) {
        model.addAttribute("seguimientos",
                seguimientoService.buscarPorUsuario(nombre));
        model.addAttribute("estados", EstadoSeguimiento.values());
        model.addAttribute("titulo", "Lista de " + nombre);
        model.addAttribute("usuario", nombre);
        return "seguimiento/lista";
    }

    //  FORMULARIO NUEVO 
    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Long recursoId,
                        Model model) {
        Seguimiento seguimiento = new Seguimiento();

        // Si viene con recursoId, precarga el recurso en el formulario
        if (recursoId != null) {
            seguimiento.setRecurso(recursoService.buscar(recursoId));
        }

        seguimiento.setFechaInicio(LocalDate.now()); // fecha de hoy por defecto
        model.addAttribute("seguimiento", seguimiento);
        model.addAttribute("recursos", recursoService.listar());
        model.addAttribute("estados", EstadoSeguimiento.values());
        model.addAttribute("titulo", "Agregar a Mi Lista");
        return "seguimiento/form";
    }

    //  FORMULARIO EDITAR 
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("seguimiento", seguimientoService.buscar(id));
        model.addAttribute("recursos", recursoService.listar());
        model.addAttribute("estados", EstadoSeguimiento.values());
        model.addAttribute("titulo", "Editar Seguimiento");
        return "seguimiento/form";
    }

    //  GUARDAR 
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Seguimiento seguimiento,
                          @RequestParam Long recursoId,
                          RedirectAttributes redirect) {
        try {
            // Asigna el recurso seleccionado en el formulario
            seguimiento.setRecurso(recursoService.buscar(recursoId));
            seguimientoService.guardar(seguimiento);
            redirect.addFlashAttribute("exito",
                    "Recurso agregado a tu lista ✅");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/seguimientos/nuevo?recursoId=" + recursoId;
        }
        return "redirect:/seguimientos";
    }

    //  CAMBIAR ESTADO
    @GetMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam String estado,
                                RedirectAttributes redirect) {
        try {
            seguimientoService.cambiarEstado(id,
                    EstadoSeguimiento.valueOf(estado));
            redirect.addFlashAttribute("exito",
                    "Estado actualizado correctamente ");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seguimientos";
    }

    //  ELIMINAR 
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            seguimientoService.eliminar(id);
            redirect.addFlashAttribute("exito",
                    "Seguimiento eliminado correctamente ✅");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seguimientos";
    }
}