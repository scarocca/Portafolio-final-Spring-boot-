package cl.sergiocarocca.cita_ideal_cl.controller;

import cl.sergiocarocca.cita_ideal_cl.entity.ItemCarrito;
import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import cl.sergiocarocca.cita_ideal_cl.service.ReservaService;
import cl.sergiocarocca.cita_ideal_cl.service.UsuarioService; // IMPORTANTE
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // IMPORTANTE
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private PlanService planService;

    @Autowired
    private UsuarioService usuarioService; // INYECTADO PARA EL PANEL

    @GetMapping("/nuevo/{planId}")
    public String mostrarFormularioReserva(@PathVariable Long planId, Model model, RedirectAttributes flash) {
        Plan planEncontrado = planService.buscarPorId(planId);
        
        if (planEncontrado == null) {
            flash.addFlashAttribute("mensajeError", "El plan seleccionado no existe.");
            return "redirect:/productos";
        }

        Reserva reserva = new Reserva();
        reserva.setPlan(planEncontrado); 
        
        model.addAttribute("plan", planEncontrado); 
        model.addAttribute("reserva", reserva);
        
        return "public/reserva-form";
    }

    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, 
                                @RequestParam("fechaSolo") String fechaSolo,
                                @RequestParam("horaFija") String horaFija,
                                Authentication authentication, // AGREGADO
                                RedirectAttributes flash, Model model) {
        try {
            // 1. Vincular Usuario Logueado para que aparezca en su panel
            if (authentication != null) {
                Usuario user = usuarioService.obtenerPorEmail(authentication.getName());
                reserva.setUsuario(user);
            }

            // 2. Parseo de Fecha
            String fechaCompleta = fechaSolo + "T" + horaFija; 
            LocalDateTime fechaExacta = LocalDateTime.parse(fechaCompleta).withSecond(0).withNano(0);
            reserva.setFechaCita(fechaExacta);
            
            reservaService.crearReserva(reserva);
            
            model.addAttribute("reserva", reserva);
            return "public/reserva-exito";
            
        } catch (RuntimeException e) {
            flash.addFlashAttribute("mensajeError", "Lo sentimos, este horario ya está reservado.");
            return "redirect:/reservas/nuevo/" + reserva.getPlan().getId();
        }
    }

    @PostMapping("/confirmar-todo")
    public String confirmarTodo(HttpSession session, 
                                @ModelAttribute Reserva datosCliente,
                                @RequestParam("fechaSolo") String fechaSolo, 
                                @RequestParam("horaFija") String horaFija, 
                                Authentication authentication, // AGREGADO
                                Model model,
                                RedirectAttributes flash) {
        
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        if (authentication == null) {
            flash.addFlashAttribute("mensajeError", "Debes iniciar sesión para reservar.");
            return "redirect:/login";
        }

        try {
            // 1. Obtener el Usuario de la BD
            Usuario usuarioLogueado = usuarioService.obtenerPorEmail(authentication.getName());

            // 2. Procesar Fecha
            String fechaCompleta = fechaSolo + "T" + horaFija;
            LocalDateTime fecha = LocalDateTime.parse(fechaCompleta);
            
            // 3. Pasar el usuario al servicio (el 4to parámetro que agregamos antes)
            List<Reserva> reservasRealizadas = reservaService.guardarReservaMultiple(carrito, fecha, datosCliente, usuarioLogueado);
            
            session.removeAttribute("carrito");
            
            model.addAttribute("reservas", reservasRealizadas);
            model.addAttribute("nombreCliente", datosCliente.getNombreCliente());
            
            return "public/reserva-exito"; 
            
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/carrito/checkout";
        }
    }

    // MÉTODOS DE ADMIN
    @GetMapping("/admin/listar")
    public String listarReservas(Model model) {
        model.addAttribute("reservas", reservaService.listarTodas());
        return "admin/dashboard-reservas";
    }

    @GetMapping("/admin/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reservaService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "Reserva eliminada.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/reservas/admin/listar";
    }

    @GetMapping("/validar-disponibilidad")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> validarDisponibilidad(
            @RequestParam Long planId, @RequestParam String fecha, @RequestParam String hora) {
        
        LocalDateTime fechaCita = LocalDateTime.parse(fecha + "T" + hora).withSecond(0).withNano(0);
        boolean ocupado = reservaService.verificarOcupado(planId, fechaCita);
        
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("disponible", !ocupado);
        return ResponseEntity.ok(respuesta);
    }
}