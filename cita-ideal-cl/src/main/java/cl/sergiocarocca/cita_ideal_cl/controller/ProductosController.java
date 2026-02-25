package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;

/**
 * Controlador responsable de la exhibición del catálogo de productos (planes).
 * Gestiona la lógica de filtrado para asegurar que solo los servicios marcados
 * como activos sean visibles para el cliente final.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
public class ProductosController {

    @Autowired
    private PlanService planService;

    /**
     * Recupera y filtra el listado de planes de servicios para su visualización pública.
     * Utiliza un stream para excluir aquellos planes que han sido desactivados
     * por la administración.
     * * @param model Objeto para inyectar la lista de planes activos en la vista.
     * @return El nombre de la plantilla HTML "productos" para renderizar el catálogo.
     */
    @GetMapping("/productos")
    public String verPaginaDeProductos(Model model) {
        // 1. Obtenemos los datos del servicio y filtramos por estado activo
        List<Plan> listaPlanes = planService.listarTodos().stream()
                .filter(Plan::isActivo)
                .toList();
        
        // 2. Pasamos la lista al HTML usando el "model"
        // El atributo "listadoDePlanes" es el que debe ser recorrido en Thymeleaf
        model.addAttribute("listadoDePlanes", listaPlanes);
        
        // 3. Devolvemos el nombre del archivo HTML
        return "productos"; 
    }
}