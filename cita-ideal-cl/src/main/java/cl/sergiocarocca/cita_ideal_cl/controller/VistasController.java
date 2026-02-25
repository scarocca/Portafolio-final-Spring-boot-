package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controlador de navegación general para vistas públicas de acceso directo.
 * Gestiona principalmente la página de inicio y el despacho de recursos
 * fundamentales para la presentación inicial de la plataforma.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
public class VistasController {

    @Autowired
    private PlanService planService;

    /**
     * Procesa la petición a la raíz de la aplicación (Landing Page).
     * Recupera el listado de todos los planes disponibles para mostrarlos
     * en la sección de servicios del index.
     * * @param model Objeto inyectado por Spring para pasar la colección de planes a la vista.
     * @return El nombre de la plantilla HTML "index" para la página de inicio.
     */
    @GetMapping("/")
    public String index(Model model) { 
        // Inyectamos la lista de planes para que el carousel o grilla del index los muestre
        model.addAttribute("planes", planService.listarTodos());
        return "index";
    }
}