package cl.sergiocarocca.cita_ideal_cl.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;
import cl.sergiocarocca.cita_ideal_cl.repository.ConsultaRepository;

/**
 * Servicio encargado de gestionar la lógica de negocio de las consultas de contacto.
 * Actúa como capa intermedia entre la capa de presentación y la persistencia,
 * proporcionando métodos para el manejo de mensajes enviados por los usuarios.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    /**
     * Constructor para la inyección de dependencias del repositorio de consultas.
     * * @param consultaRepository El repositorio encargado del acceso a datos.
     */
    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    /**
     * Recupera el listado completo de consultas registradas en el sistema.
     * * @return Una lista de objetos {@link Consulta}.
     */
    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    /**
     * Procesa y persiste una nueva consulta en la base de datos.
     * Asigna automáticamente la fecha y hora actual del servidor antes de guardar.
     * * @param consulta El objeto consulta a registrar.
     */
    public void guardar(Consulta consulta) {
        consulta.setFechaEnvio(LocalDateTime.now());
        consultaRepository.save(consulta);
    }

    /**
     * Elimina una consulta específica basándose en su identificador único.
     * * @param id El ID de la consulta que se desea borrar.
     */
    public void eliminar(Long id) {
        consultaRepository.deleteById(id);
    }
    
    /**
     * Obtiene el número total de consultas recibidas hasta la fecha.
     * Útil para generar estadísticas en el dashboard administrativo.
     * * @return Cantidad total de registros en la tabla de consultas.
     */
    public long contarTodas() {
        return consultaRepository.count();
    }
}