package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cl.sergiocarocca.cita_ideal_cl.entity.ItemCarrito;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador encargado de gestionar el flujo de pago simulado.
 * Orquestra la transición entre el formulario de reserva, la pasarela de pago
 * y la pantalla intermedia de procesamiento (spinner) para ofrecer una experiencia
 * de usuario realista.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
@RequestMapping("/pago")
public class PagoController {

    /**
     * Paso 1: Muestra la pasarela de pago para la compra de un único plan.
     * * @param reserva Objeto reserva con los datos del cliente recolectados previamente.
     * @param planId Identificador del plan seleccionado.
     * @param fechaSolo Fecha de la cita en formato String.
     * @param horaFija Bloque horario seleccionado para la cita.
     * @param model Objeto para transferir los datos de la reserva a la pasarela.
     * @return El nombre de la plantilla HTML para la pasarela de pago simulada.
     */
    @PostMapping("/procesar")
    public String mostrarPasarela(@ModelAttribute Reserva reserva, 
                                 @RequestParam Long planId,
                                 @RequestParam String fechaSolo,
                                 @RequestParam String horaFija,
                                 Model model) {
        
        model.addAttribute("reserva", reserva);
        model.addAttribute("planId", planId);
        model.addAttribute("fechaSolo", fechaSolo);
        model.addAttribute("horaFija", horaFija);
        
        return "public/pasarela-simulada";
    }

    /**
     * Paso 2: Recibe los datos de pago y redirige a una pantalla de espera.
     * Esta etapa simula la validación bancaria mostrando un spinner de carga.
     * * @param reserva Objeto reserva que persiste durante el flujo.
     * @param planId Identificador del plan.
     * @param fechaSolo Fecha de la cita.
     * @param horaFija Hora de la cita.
     * @param model Objeto para pasar los datos al formulario invisible del procesando.
     * @return El nombre de la plantilla HTML con la animación de carga.
     */
    @PostMapping("/confirmar-final")
    public String procesarEspera(@ModelAttribute Reserva reserva, 
                                @RequestParam Long planId,
                                @RequestParam String fechaSolo,
                                @RequestParam String horaFija,
                                Model model) {
        model.addAttribute("reserva", reserva);
        model.addAttribute("planId", planId);
        model.addAttribute("fechaSolo", fechaSolo);
        model.addAttribute("horaFija", horaFija);
        
        return "public/pago-procesando"; 
    }

    /**
     * Paso 1 (Carrito): Muestra la pasarela para compras múltiples desde el carrito.
     * * @param reserva Objeto reserva con la información del contacto.
     * @param fechaSolo Fecha seleccionada para la atención de los servicios.
     * @param horaFija Hora seleccionada para el inicio de los servicios.
     * @param session Sesión HTTP para recuperar los ítems actuales del carrito.
     * @param model Objeto para enviar la información y la bandera de carrito a la vista.
     * @return El nombre de la plantilla HTML para la pasarela de pago.
     */
    @PostMapping("/procesar-carrito")
    public String mostrarPasarelaCarrito(@ModelAttribute Reserva reserva, 
                                         @RequestParam String fechaSolo,
                                         @RequestParam String horaFija,
                                         HttpSession session,
                                         Model model) {
        
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        model.addAttribute("reserva", reserva);
        model.addAttribute("fechaSolo", fechaSolo);
        model.addAttribute("horaFija", horaFija);
        model.addAttribute("esCarrito", true); 
        
        return "public/pasarela-simulada";
    }

    /**
     * Paso 2 (Carrito): Muestra la pantalla de procesamiento para compras del carrito.
     * * @param reserva Objeto reserva final.
     * @param fechaSolo Fecha de la cita.
     * @param horaFija Hora de la cita.
     * @param model Objeto para transferir los datos al spinner de procesamiento.
     * @return El nombre de la plantilla HTML con la animación de carga para carrito.
     */
    @PostMapping("/confirmar-final-carrito")
    public String procesarEsperaCarrito(@ModelAttribute Reserva reserva, 
                                        @RequestParam String fechaSolo,
                                        @RequestParam String horaFija,
                                        Model model) {
        
        model.addAttribute("reserva", reserva);
        model.addAttribute("fechaSolo", fechaSolo);
        model.addAttribute("horaFija", horaFija);
        model.addAttribute("esCarrito", true); 
        
        return "public/pago-procesando"; 
    }
}