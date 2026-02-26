package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import cl.sergiocarocca.cita_ideal_cl.service.ReservaService;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.service.ConsultaService;

@Controller
public class DashboardController {

    private final ReservaService reservaService;
    private final ConsultaService consultaService;

    public DashboardController(ReservaService reservaService, ConsultaService consultaService) {
        this.reservaService = reservaService;
        this.consultaService = consultaService;
    }

    @GetMapping("/panel")
    public String mostrarPanel(Model model, Authentication authentication) {
        // 1. Verificación de seguridad: si no hay sesión, al login
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // 2. Obtener el identificador (Email)
        String email = authentication.getName();
        
        // 3. Obtener los datos usando los servicios
        List<Reserva> misReservas = reservaService.obtenerPorEmail(email);
        var misConsultas = consultaService.obtenerPorEmail(email);

        // 4. Pasar al modelo con nombres claros
        model.addAttribute("reservas", misReservas);
        model.addAttribute("consultas", misConsultas);
        
        // 5. Retornar la vista (está en templates/dashboard.html)
        return "dashboard";
    }
}