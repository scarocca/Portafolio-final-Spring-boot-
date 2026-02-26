package cl.sergiocarocca.cita_ideal_cl.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.service.ConsultaService;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import jakarta.validation.Valid;

/**
 * Controlador administrativo para la gestión de planes de servicios.
 * Permite realizar operaciones CRUD, gestionar imágenes asociadas a cada plan
 * y controlar la visibilidad pública de los mismos.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/planes")
public class AdminPlanesController {

    @Autowired
    private PlanService planService;

    @Autowired
    private ConsultaService consultaService;
    
    /** Ruta relativa donde se almacenan físicamente las imágenes de los planes. */
    private final String carpetaPlanes = "src/main/resources/static/assets/img/planes/";

    /**
     * Lista todos los planes registrados y muestra el conteo total de consultas en el panel.
     * * @param model Objeto para enviar la lista de planes y estadísticas a la vista.
     * @return El nombre de la plantilla para el listado de planes.
     */
    @GetMapping
    public String listarPlanes(Model model) {
        model.addAttribute("planes", planService.listarTodos());
        long totalConsultas = consultaService.listarTodas().size(); 
        model.addAttribute("totalConsultas", totalConsultas);
        return "admin/planes-lista";
    }

    /**
     * Prepara el formulario para la creación de un nuevo plan de servicio.
     * * @param model Objeto para inicializar un objeto Plan vacío en la vista.
     * @return El nombre de la plantilla del formulario de planes.
     */
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("plan", new Plan());
        return "admin/plan-form";
    }

    /**
     * Recupera un plan específico para su edición.
     * * @param id Identificador único del plan a editar.
     * @param model Objeto para pasar los datos del plan encontrado a la vista.
     * @return La plantilla del formulario o redirección si el ID no existe.
     */
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Plan plan = planService.buscarPorId(id);
        if (plan != null) {
            model.addAttribute("plan", plan);
            return "admin/plan-form";
        }
        return "redirect:/admin/planes";
    }

    /**
     * Elimina permanentemente un plan de la base de datos.
     * * @param id Identificador del plan a eliminar.
     * @return Redirección al listado actualizado de planes.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            // 1. Buscamos el plan para saber el nombre de la imagen
            Plan plan = planService.buscarPorId(id);

            if (plan != null) {
                // 2. Lógica de borrado físico del archivo
                if (plan.getImagenUrl() != null && !plan.getImagenUrl().isEmpty()) {
                    Path rutaArchivo = Paths.get(carpetaPlanes)
                                            .toAbsolutePath()
                                            .resolve(plan.getImagenUrl());
                    
                    // Borramos el archivo físico
                    Files.deleteIfExists(rutaArchivo);
                }

                // 3. Borramos el registro de la base de datos
                planService.eliminar(id);
                flash.addFlashAttribute("success", "El plan y su imagen han sido eliminados.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            flash.addFlashAttribute("error", "Error al eliminar el archivo físico.");
        }

        return "redirect:/admin/planes";
    }

    /**
     * Procesa el guardado de un plan nuevo o existente, incluyendo la validación de datos
     * y la carga obligatoria de imagen para nuevos registros.
     * * @param plan Objeto Plan vinculado al formulario.
     * @param result Resultado de la validación de campos obligatorios.
     * @param archivo Archivo de imagen subido por el usuario.
     * @param flash Atributos para mensajes de éxito o error.
     * @param model Objeto para reinyectar errores de imagen en la vista.
     * @return Redirección al listado o retorno al formulario en caso de error.
     */
    @PostMapping("/guardar")
    public String guardarPlan(@Valid @ModelAttribute Plan plan,
                              BindingResult result,    
                              @RequestParam("archivoImagen") MultipartFile archivo,
                              RedirectAttributes flash, Model model) {
        
        // 1. Validar errores de campos (nombre, precio, etc.)
        if (result.hasErrors()) {
            return "admin/plan-form"; 
        }
        
        // 2. Validar imagen obligatoria solo si es un plan NUEVO
        if (plan.getId() == null && archivo.isEmpty()) {
            model.addAttribute("errorImagen", "La imagen es obligatoria para nuevos planes");
            return "admin/plan-form";
        }

        try {
            Plan planExistente = null;
            if (plan.getId() != null) {
                planExistente = planService.buscarPorId(plan.getId());
            }

            if (!archivo.isEmpty()) {
                // Lógica de guardado de imagen nueva
                Path rutaAbsoluta = Paths.get(carpetaPlanes).toAbsolutePath();
                if (!Files.exists(rutaAbsoluta)) Files.createDirectories(rutaAbsoluta);

                // Borrar imagen vieja si existe y estamos editando
                if (planExistente != null && planExistente.getImagenUrl() != null) {
                    Path rutaFotoVieja = rutaAbsoluta.resolve(planExistente.getImagenUrl());
                    Files.deleteIfExists(rutaFotoVieja);
                }

                String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                Files.write(rutaAbsoluta.resolve(nombreArchivo), archivo.getBytes());
                plan.setImagenUrl(nombreArchivo);

            } else if (planExistente != null) {
                // Si NO hay archivo nuevo pero es EDICIÓN, mantenemos la que ya tenía
                plan.setImagenUrl(planExistente.getImagenUrl());
            }

            planService.guardar(plan);
            flash.addFlashAttribute("success", plan.getId() == null ? "¡Plan creado!" : "¡Plan actualizado!");

        } catch (IOException e) {
            flash.addFlashAttribute("error", "Error al procesar archivos: " + e.getMessage());
        }

        return "redirect:/admin/planes";
    }

    /**
     * Cambia el estado del plan a oculto para que no aparezca en el catálogo público.
     * * @param id ID del plan.
     * @param flash Mensaje de confirmación.
     * @return Redirección a la lista de planes.
     */
    @GetMapping("/ocultar/{id}")
    public String ocultarPlan(@PathVariable Long id, RedirectAttributes flash) {
        planService.ocultarPlan(id);
        flash.addFlashAttribute("success", "El plan ha sido ocultado de la vista pública.");
        return "redirect:/admin/planes";
    }

    /**
     * Cambia el estado del plan a activo para que sea visible en el catálogo público.
     * * @param id ID del plan.
     * @param flash Mensaje de confirmación.
     * @return Redirección a la lista de planes.
     */
    @GetMapping("/activar/{id}")
    public String activarPlan(@PathVariable Long id, RedirectAttributes flash) {
        planService.activarPlan(id);
        flash.addFlashAttribute("mensajeExito", "El plan ahora es visible nuevamente en el catálogo.");
        return "redirect:/admin/planes";
    }
}