package cl.sergiocarocca.cita_ideal_cl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;

/**
 * Repositorio de acceso a datos para la entidad {@link Consulta}.
 * Proporciona la abstracción necesaria para realizar operaciones CRUD sobre la tabla
 * de consultas, utilizando las capacidades de Spring Data JPA para la generación 
 * automática de consultas SQL.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    /**
     * Recupera todas las consultas almacenadas, ordenándolas de forma descendente 
     * según su fecha de envío (de la más reciente a la más antigua).
     * * @return Una lista de {@link Consulta} ordenada por fecha de envío de forma cronológica inversa.
     */
    List<Consulta> findAllByOrderByFechaEnvioDesc();
    List<Consulta> findByUsuarioEmail(String email);
}