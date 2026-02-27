package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;
import cl.sergiocarocca.cita_ideal_cl.entity.MensajeChat;
import cl.sergiocarocca.cita_ideal_cl.service.ConsultaService;
import cl.sergiocarocca.cita_ideal_cl.service.ChatService;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ConsultaService consultaService;

    @PostMapping("/consultas/enviar-mensaje")
    public String enviarMensaje(@RequestParam Long consultaId, 
                                @RequestParam String contenido, 
                                Authentication auth) {
        
        Consulta consulta = consultaService.buscarPorId(consultaId);
        
        MensajeChat nuevoMensaje = new MensajeChat();
        nuevoMensaje.setContenido(contenido);
        nuevoMensaje.setConsulta(consulta);
        
        // Verificamos si el que escribe es el administrador
        boolean isAdmin = auth.getAuthorities().stream()
                              .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        nuevoMensaje.setEsAdmin(isAdmin);
        
        chatService.guardarMensaje(nuevoMensaje);
        
        // Redirección dinámica: si es admin vuelve a su panel, si es usuario al suyo
        return isAdmin ? "redirect:/admin/consultas" : "redirect:/panel";
    }
    @PostMapping("/consultas/borrar-chat")
    public String borrarChat(@RequestParam Long consultaId, Authentication auth) {
        chatService.borrarHistorialPorConsulta(consultaId);
        
        // Redirección inteligente
        boolean isAdmin = auth.getAuthorities().stream()
                              .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        return isAdmin ? "redirect:/admin/consultas" : "redirect:/panel";
    }
}