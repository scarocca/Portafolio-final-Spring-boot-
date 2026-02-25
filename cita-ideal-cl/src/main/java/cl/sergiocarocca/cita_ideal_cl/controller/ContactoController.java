package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;
import cl.sergiocarocca.cita_ideal_cl.repository.ConsultaRepository;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;

/**
 * Controlador encargado de gestionar las interacciones del formulario de contacto.
 * Procesa las inquietudes de los usuarios y permite vincular consultas a planes
 * específicos de servicios ofrecidos en la plataforma.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
public class ContactoController {

    @Autowired
    private ConsultaRepository consultaRepository;
    
    @Autowired
    private PlanService planService;

    /**
     * Procesa y almacena una nueva consulta enviada por un usuario.
     * Si se proporciona un ID de plan válido, la consulta queda vinculada a dicho plan
     * para una atención personalizada.
     * * @param nombre Nombre del remitente de la consulta.
     * @param email Dirección de correo electrónico para contacto posterior.
     * @param planId (Opcional) Identificador del plan por el cual el usuario está interesado.
     * @param mensaje Contenido detallado de la consulta o inquietud.
     * @param redirect Objeto para enviar mensajes de éxito (flash attributes) tras la redirección.
     * @return Redirección a la página de inicio con un mensaje de confirmación.
     */
    @PostMapping("/contacto/enviar")
    public String procesarConsulta(@RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam(required = false) Long planId,
                                   @RequestParam String mensaje,
                                   RedirectAttributes redirect) {
        
        Consulta nuevaConsulta = new Consulta();
        nuevaConsulta.setNombre(nombre);
        nuevaConsulta.setEmail(email);
        nuevaConsulta.setMensaje(mensaje);

        // Si seleccionó un plan, lo buscamos y lo asociamos
        if (planId != null && planId != 0) {
            nuevaConsulta.setPlan(planService.buscarPorId(planId));
        }

        consultaRepository.save(nuevaConsulta);

        // Mensaje de éxito para el usuario
        redirect.addFlashAttribute("mensajeExito", "¡Gracias! Tu consulta ha sido enviada. Te contactaremos pronto.");
        
        return "redirect:/";
    }
}