package cl.sergiocarocca.cita_ideal_cl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Reserva}.
 * Gestiona la persistencia de las citas programadas, proporcionando métodos 
 * para la validación de disponibilidad horaria y búsqueda avanzada por 
 * criterios específicos del cliente.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Verifica la disponibilidad de un plan para una fecha y hora específicas.
     * Realiza una consulta personalizada (JPQL) para determinar si existe alguna 
     * reserva previa con estado 'CONFIRMADA' que coincida con los parámetros.
     * * @param planId Identificador único del plan a validar.
     * @param fecha Marca de tiempo (fecha y hora) de la cita solicitada.
     * @return true si ya existe una reserva confirmada para ese horario, false en caso contrario.
     */
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.plan.id = :planId " +
           "AND r.fechaCita = :fecha " +
           "AND r.estado = 'CONFIRMADA'")
    boolean existeReservaEnEsaFecha(@Param("planId") Long planId, 
                                    @Param("fecha") LocalDateTime fecha);

    /**
     * Recupera todas las reservas asociadas a la dirección de correo electrónico de un cliente.
     * * @param email Correo electrónico del cliente.
     * @return Una lista de {@link Reserva} vinculadas al email proporcionado.
     */
    List<Reserva> findByEmailCliente(String email);
    
    /**
     * Busca una reserva específica utilizando su código de seguimiento alfanumérico.
     * * @param codigo El código único de seguimiento generado al momento de la reserva.
     * @return La instancia de {@link Reserva} encontrada o null si el código no existe.
     */
    Reserva findByCodigoSeguimiento(String codigo);
    
    /**
     * Obtiene el listado global de reservas ordenadas de forma cronológica inversa.
     * Utilizado principalmente para el historial en el panel administrativo.
     * * @return Lista de reservas ordenadas desde la más futura a la más antigua.
     */
    List<Reserva> findAllByOrderByFechaCitaDesc();
}