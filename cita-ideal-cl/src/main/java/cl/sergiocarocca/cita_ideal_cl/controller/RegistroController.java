package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.service.UsuarioService;

/**
 * Controlador encargado de gestionar el registro de nuevos usuarios en la plataforma.
 * Permite la creación de cuentas de acceso público, manejando validaciones de 
 * negocio y persistencia de perfiles de usuario.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    /**
     * Constructor para la inyección de dependencias del servicio de usuarios.
     * * @param usuarioService Servicio que contiene la lógica para el registro y gestión de usuarios.
     */
    public RegistroController(UsuarioService usuarioService) {
        super();
        this.usuarioService = usuarioService;
    }

    /**
     * Prepara y muestra el formulario de registro para un nuevo usuario.
     * Inicializa un objeto {@link Usuario} vacío para vincularlo con el formulario en la vista.
     * * @param model Objeto para inyectar el nuevo usuario en el contexto de Thymeleaf.
     * @return El nombre de la plantilla HTML "/registro".
     */
    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        // Pasamos un objeto Usuario vacío para que Thymeleaf lo llene
        model.addAttribute("usuario", new Usuario());
        return "/registro";
    }

    /**
     * Procesa la solicitud de creación de una nueva cuenta de usuario.
     * Gestiona excepciones de validación (como contraseñas no coincidentes) o duplicidad
     * de registros, devolviendo al usuario al formulario con mensajes de error descriptivos.
     * * @param usuario Objeto usuario poblado con los datos del formulario.
     * @param model Objeto para reinyectar mensajes de error en la vista en caso de falla.
     * @param redirect Atributos flash para enviar mensajes de éxito tras la redirección al login.
     * @return Redirección al login en caso de éxito, o retorno al formulario en caso de error.
     */
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario,
                                 Model model,
                                 RedirectAttributes redirect) {
        try {
            // Se registra el usuario a través del servicio con rol público por defecto
            usuarioService.registrarUsuarioPublico(usuario);
            
            redirect.addFlashAttribute("mensajeExito", "¡Registro exitoso! Bienvenido a Tu Cita Ideal");
            
            return "redirect:/login";
            
        } catch (IllegalArgumentException e) {
            // Capturamos errores específicos de lógica de negocio (ej: contraseñas)
            model.addAttribute("error", e.getMessage());
            return "registro"; 
        } catch (Exception e) {
            // Capturamos errores genéricos o de persistencia (ej: email ya existe)
            model.addAttribute("error", "Error al registrar: El usuario ya podría estar registrado.");
            return "registro";
        }
    }
}