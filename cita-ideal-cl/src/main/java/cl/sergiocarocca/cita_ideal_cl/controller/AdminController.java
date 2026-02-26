	package cl.sergiocarocca.cita_ideal_cl.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.repository.ConsultaRepository;
import cl.sergiocarocca.cita_ideal_cl.service.ConsultaService;
import cl.sergiocarocca.cita_ideal_cl.service.ReservaService;
import cl.sergiocarocca.cita_ideal_cl.service.UsuarioService;

/**
 * Controlador encargado de gestionar las funcionalidades del panel de administración.
 * Permite la visualización de consultas, gestión de reservas y administración de usuarios.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ConsultaRepository consultaRepository;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final ConsultaService consultaService;
    /**
     * Constructor para la inyección de dependencias.
     * * @param consultaRepository Repositorio para la gestión de consultas de contacto.
     * @param reservaService Servicio para la lógica de negocio de reservas.
     * @param usuarioService Servicio para la gestión de cuentas de usuario.
     */
    public AdminController(ConsultaRepository consultaRepository, ReservaService reservaService,
            UsuarioService usuarioService,ConsultaService consultaService) {
        super();
        this.consultaRepository = consultaRepository;
        this.reservaService = reservaService;
        this.usuarioService = usuarioService;
        this.consultaService = consultaService;
    }

    /**
     * Lista todas las consultas recibidas, ordenadas desde la más reciente a la más antigua.
     * * @param model Objeto para pasar datos a la vista.
     * @return El nombre de la plantilla HTML para listar consultas.
     */
    @GetMapping("/consultas")
    public String listarConsultas(Model model) {
        List<Consulta> lista = consultaRepository.findAll();
        Collections.reverse(lista); 
        model.addAttribute("consultas", lista);
        return "admin/consultas-lista";
    }

    /**
     * Obtiene y muestra el listado global de reservas en el dashboard administrativo.
     * * @param model Objeto para pasar datos a la vista.
     * @return El nombre de la plantilla HTML del dashboard de reservas.
     */
    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        List<Reserva> listaReservas = reservaService.listarTodas();
        model.addAttribute("reservas", listaReservas);
        model.addAttribute("seccion", "reservas"); 
        return "admin/dashboard-reservas"; 
    }

    /**
     * Recupera la lista completa de usuarios registrados en el sistema.
     * * @param model Objeto para pasar datos a la vista.
     * @return El nombre de la plantilla HTML para la gestión de usuarios.
     */
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> lista = usuarioService.listartodo();
        model.addAttribute("usuarios", lista);
        return "admin/usuario-lista";
    }
    @GetMapping("/reservas/confirmar/{id}")
    public String confirmarReserva(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reservaService.confirmarReserva(id);
            flash.addFlashAttribute("success", "¡Reserva confirmada exitosamente!");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo confirmar la reserva.");
        }
        // Redirigimos a la misma vista de reservas para ver el cambio
        return "redirect:/admin/reservas"; 
    }

    /**
     * Elimina un usuario del sistema basado en su ID único.
     * * @param id El identificador único del usuario a eliminar.
     * @param flash Objeto para enviar mensajes de éxito o error tras la redirección.
     * @return Redirección a la lista general de usuarios.
     */
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes flash) {
        try {
            usuarioService.eliminar(id);
            flash.addFlashAttribute("success", "Usuario eliminado con éxito.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el usuario.");
        }
        return "redirect:/admin/usuarios";
    }
    @PostMapping("/consultas/responder")
    public String responder(@RequestParam Long consultaId, @RequestParam String respuesta, RedirectAttributes flash) {
        Consulta consulta = consultaService.buscarPorId(consultaId);
        consulta.setRespuesta(respuesta);
        consulta.setFechaRespuesta(LocalDateTime.now());
        
        consultaService.guardar(consulta);
        
        flash.addFlashAttribute("mensajeExito", "¡Respuesta guardada con éxito!");
        return "redirect:/admin/consultas"; // Ajusta a tu ruta real de listado
    }
}