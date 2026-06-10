package com.tomas.cshub.controller;

import com.tomas.cshub.service.RecursoService;
import com.tomas.cshub.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @Autowired
    private RecursoService recursoService;

    @Autowired
    private SeguimientoService seguimientoService;

    // GET / → página de inicio
    @GetMapping("/")
    public String inicio(Model model) {
        // Envía los 6 recursos más recientes a la página de inicio
        model.addAttribute("recursosRecientes",
                recursoService.listarRecientes()
                .stream().limit(6).toList());
        model.addAttribute("totalRecursos",
                recursoService.listar().size());
        model.addAttribute("totalSeguimientos",
                seguimientoService.listar().size());
        return "index";
    }
}