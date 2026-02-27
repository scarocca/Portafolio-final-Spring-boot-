package cl.sergiocarocca.cita_ideal_cl.service;

import cl.sergiocarocca.cita_ideal_cl.entity.MensajeChat;
import cl.sergiocarocca.cita_ideal_cl.repository.MensajeChatRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private MensajeChatRepository chatRepo;

    public void guardarMensaje(MensajeChat mensaje) {
        chatRepo.save(mensaje);
    }
    @Transactional
    public void borrarHistorialPorConsulta(Long consultaId) {
        // Esto asume que tienes el método en el repositorio
        chatRepo.deleteByConsultaId(consultaId);
    }
}