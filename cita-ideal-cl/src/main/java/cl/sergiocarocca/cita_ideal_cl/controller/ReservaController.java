package cl.sergiocarocca.cita_ideal_cl.controller;

import cl.sergiocarocca.cita_ideal_cl.entity.ItemCarrito;
import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import cl.sergiocarocca.cita_ideal_cl.service.ReservaService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador principal para la gestión de reservas de servicios.
 * Administra el ciclo de vida de una reserva, desde la selección de horarios
 * hasta la persistencia final, permitiendo tanto compras individuales como
 * reservas múltiples procesadas a través del carrito.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private PlanService planService;

    /**
     * Prepara el formulario de reserva para un servicio específico.
     * Valida la existencia del plan antes de proceder para asegurar la integridad de los datos.
     * * @param planId Identificador del plan a reservar.
     * @param model Contenedor para el objeto {@link Reserva} y el {@link Plan} encontrado.
     * @param flash Mensajes de retroalimentación en caso de que el plan no sea válido.
     * @return Vista del formulario de reserva o redirección al catálogo en caso de error.
     */
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

    /**
     * Procesa y guarda una reserva individual.
     * Realiza la unión técnica de los inputs de fecha (ISO-DATE) y hora (HH:mm) para
     * construir el objeto {@link LocalDateTime} requerido por la entidad.
     * * @param reserva Objeto reserva vinculado al formulario.
     * @param planId Identificador del plan (enviado de forma oculta o vía parámetro).
     * @param fechaSolo Cadena de texto con la fecha (YYYY-MM-DD).
     * @param horaFija Cadena de texto con el bloque horario (HH:mm).
     * @param model Objeto para pasar los detalles de éxito a la vista final.
     * @param flash Mensajes de error en caso de fallo en el parseo o persistencia.
     * @return Vista de éxito de reserva o redirección al catálogo si ocurre una excepción.
     */
    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, 
                                @RequestParam("fechaSolo") String fechaSolo,
                                @RequestParam("horaFija") String horaFija,
                                RedirectAttributes flash, Model model) {
        try {
            // 1. Unimos fecha y hora
            String fechaCompleta = fechaSolo + "T" + horaFija; 
            
            // 2. IMPORTANTE: Limpiamos segundos y nanosegundos para que la comparación sea exacta
            LocalDateTime fechaExacta = LocalDateTime.parse(fechaCompleta)
                                                    .withSecond(0)
                                                    .withNano(0);
            
            reserva.setFechaCita(fechaExacta);
            
            // 3. Llamamos al servicio (que hará el COUNT en el repositorio)
            reservaService.crearReserva(reserva);
            
            model.addAttribute("reserva", reserva);
            return "public/reserva-exito";
            
        } catch (RuntimeException e) {
            // Si el servicio lanza la excepción porque el COUNT fue > 0
            flash.addFlashAttribute("mensajeError", "Lo sentimos, este horario ya está reservado para este plan.");
            return "redirect:/reservas/nuevo/" + reserva.getPlan().getId();
        }
    }

    /**
     * Procesa la confirmación masiva de todos los ítems contenidos en el carrito.
     * Al finalizar con éxito, limpia el carrito de la sesión HTTP.
     * * @param session Sesión actual para obtener la lista de {@link ItemCarrito}.
     * @param datosCliente Objeto reserva que contiene los datos de contacto unificados.
     * @param fechaSolo Fecha seleccionada para todos los servicios.
     * @param horaFija Hora de inicio seleccionada.
     * @param model Contenedor para la lista de reservas realizadas con éxito.
     * @param flash Mensajes de error para redirección al checkout en caso de fallo.
     * @return Vista de éxito para múltiples reservas.
     */
    @PostMapping("/confirmar-todo")
    public String confirmarTodo(HttpSession session, 
                                @ModelAttribute Reserva datosCliente,
                                @RequestParam("fechaSolo") String fechaSolo, 
                                @RequestParam("horaFija") String horaFija,    
                                Model model,
                                RedirectAttributes flash) {
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        try {
            String fechaCompleta = fechaSolo + "T" + horaFija;
            LocalDateTime fecha = LocalDateTime.parse(fechaCompleta);
            
            // Delegación de lógica de negocio compleja al servicio
            List<Reserva> reservasRealizadas = reservaService.guardarReservaMultiple(carrito, fecha, datosCliente);
            
            session.removeAttribute("carrito");
            
            model.addAttribute("reservas", reservasRealizadas);
            model.addAttribute("nombreCliente", datosCliente.getNombreCliente());
            
            return "public/reserva-exito"; 
            
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/carrito/checkout";
        }
    }

    /**
     * Lista todas las reservas existentes para el panel administrativo.
     * * @param model Objeto para pasar la colección completa de reservas a la vista.
     * @return Vista administrativa con la tabla de gestión de reservas.
     */
    @GetMapping("/admin/listar")
    public String listarReservas(Model model) {
        List<Reserva> lista = reservaService.listarTodas();
        model.addAttribute("reservas", lista);
        return "admin/dashboard-reservas";
    }

    /**
     * Elimina una reserva específica del sistema.
     * * @param id Identificador de la reserva a remover.
     * @param flash Atributos para notificar el éxito o el error de la operación.
     * @return Redirección a la lista administrativa de reservas.
     */
    @GetMapping("/admin/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reservaService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "La reserva ha sido eliminada correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al intentar eliminar: " + e.getMessage());
        }
        return "redirect:/reservas/admin/listar";
    }
    @GetMapping("/validar-disponibilidad")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> validarDisponibilidad(
            @RequestParam Long planId, 
            @RequestParam String fecha, 
            @RequestParam String hora) {
        
        LocalDateTime fechaCita = LocalDateTime.parse(fecha + "T" + hora)
                                               .withSecond(0)
                                               .withNano(0);
                                               
        boolean ocupado = reservaService.verificarOcupado(planId, fechaCita);
        
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("disponible", !ocupado);
        return ResponseEntity.ok(respuesta);
    }
}