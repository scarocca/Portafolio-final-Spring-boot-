package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador encargado de gestionar el acceso al sistema.
 * Proporciona el punto de entrada para la autenticación de usuarios
 * y la visualización del formulario de inicio de sesión.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
public class LoginController {

    /**
     * Muestra la vista del formulario de inicio de sesión.
     * Este endpoint es utilizado generalmente por Spring Security como la 
     * página de login personalizada.
     * * @return El nombre de la plantilla HTML "login" que contiene el formulario.
     */
    @GetMapping("/login")
    public String login() {
        return "login"; 
    }
}