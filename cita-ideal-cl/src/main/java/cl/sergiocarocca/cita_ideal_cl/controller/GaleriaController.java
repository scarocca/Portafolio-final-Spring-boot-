package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cl.sergiocarocca.cita_ideal_cl.entity.Foto;
import cl.sergiocarocca.cita_ideal_cl.service.FotoService;

/**
 * Controlador encargado de la visualización pública de la galería de imágenes.
 * Proporciona el acceso a los recursos multimedia para ser renderizados en la 
 * interfaz de usuario final.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
public class GaleriaController {

    @Autowired
    private FotoService fotoService;

    /**
     * Constructor para la inyección de dependencias de los servicios necesarios.
     * * @param fotoService Servicio que gestiona la lógica de las fotografías.
     */
    public GaleriaController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    /**
     * Recupera todas las fotografías registradas y las prepara para su visualización.
     * Incluye una validación de seguridad para evitar errores de renderizado en la 
     * plantilla en caso de que la colección de fotos sea nula.
     * * @param model Objeto para transportar la lista de fotos hacia la vista.
     * @return El nombre de la plantilla HTML "galeria" para el catálogo de fotos.
     */
    @GetMapping("/galeria")
    public String verGaleria(Model model) {
        // Obtenemos la lista de fotos desde el servicio
        List<Foto> lista = fotoService.listarTodas();
        
        // Validación para asegurar que Thymeleaf siempre reciba un iterable válido
        if (lista == null) {
            lista = new ArrayList<>();
        }
        
        model.addAttribute("fotos", lista); 
        return "galeria"; 
    }
}