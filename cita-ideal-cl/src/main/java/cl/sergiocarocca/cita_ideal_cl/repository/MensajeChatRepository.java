package cl.sergiocarocca.cita_ideal_cl.repository;

import cl.sergiocarocca.cita_ideal_cl.entity.MensajeChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {
    // Esto nos servirá para traer el historial de una consulta específica ordenado por fecha
    List<MensajeChat> findByConsultaIdOrderByFechaEnvioAsc(Long consultaId);
    void deleteByConsultaId(Long consultaId);
}