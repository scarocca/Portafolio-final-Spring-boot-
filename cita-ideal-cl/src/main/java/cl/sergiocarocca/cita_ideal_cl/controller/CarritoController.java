package cl.sergiocarocca.cita_ideal_cl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import cl.sergiocarocca.cita_ideal_cl.entity.ItemCarrito;
import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador para gestionar el carrito de compras de la aplicación.
 * Utiliza la sesión HTTP para persistir los ítems seleccionados por el usuario
 * antes de proceder al proceso de reserva o checkout.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private PlanService planService;

    /**
     * Agrega un plan al carrito de compras. Si el plan ya existe en la sesión,
     * incrementa su cantidad; de lo contrario, crea un nuevo registro.
     * * @param id Identificador único del plan a agregar.
     * @param session Sesión HTTP para almacenar la lista de ítems.
     * @return Redirección a la vista detallada del carrito.
     */
    @GetMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        Plan planDeseado = planService.buscarPorId(id);

        if (planDeseado != null) {
            boolean existe = false;
            for (ItemCarrito item : carrito) {
                if (item.getPlan().getId().equals(id)) {
                    item.setCantidad(item.getCantidad() + 1);
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                carrito.add(new ItemCarrito(planDeseado, 1));
            }
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/carrito/ver";
    }

    /**
     * Recupera el contenido actual del carrito de la sesión y calcula el total acumulado.
     * * @param model Objeto para enviar la lista de ítems y el cálculo del total a la vista.
     * @param session Sesión HTTP donde reside el carrito.
     * @return El nombre de la plantilla HTML para visualizar el carrito.
     */
    @GetMapping("/ver")
    public String verCarrito(Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        
        int itemsCount = (carrito != null) ? carrito.size() : 0;
        model.addAttribute("itemsCount", itemsCount);

        BigDecimal total = carrito.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", total);

        return "carrito-vista";
    }

    /**
     * Remueve un ítem específico del carrito basado en el ID del plan.
     * * @param id Identificador del plan que se desea quitar del carrito.
     * @param session Sesión HTTP actualizada.
     * @return Redirección a la vista del carrito tras la eliminación.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        if (carrito != null) {
            carrito.removeIf(item -> item.getPlan().getId().equals(id));
            session.setAttribute("carrito", carrito);
        }
        
        return "redirect:/carrito/ver";
    }

    /**
     * Limpia completamente el carrito de la sesión del usuario.
     * * @param session Sesión HTTP a la cual se le removerá el atributo "carrito".
     * @return Redirección a la vista del carrito vacío.
     */
    @GetMapping("/vaciar")
    public String vaciarCarrito(HttpSession session) {
        session.removeAttribute("carrito");
        return "redirect:/carrito/ver";
    }

    /**
     * Prepara la información necesaria para el proceso final de reserva,
     * calculando montos y enviando un objeto Reserva vacío para el formulario.
     * * @param session Sesión HTTP para validar si el carrito tiene ítems.
     * @param model Objeto para pasar los datos de la compra al formulario final.
     * @return La vista de confirmación de reserva o redirección si el carrito está vacío.
     */
    @GetMapping("/checkout")
    public String irACheckout(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/productos";
        }

        BigDecimal total = carrito.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("reserva", new Reserva()); 
        model.addAttribute("carrito", carrito); 
        model.addAttribute("totalCarrito", total);
        
        return "public/reserva-confirmacion-carrito"; 
    }
}