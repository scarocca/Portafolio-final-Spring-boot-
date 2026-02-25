package cl.sergiocarocca.cita_ideal_cl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;

/**
 * Repositorio de acceso a datos para la entidad {@link Plan}.
 * Esta interfaz hereda todas las capacidades de Spring Data JPA, permitiendo
 * gestionar de forma eficiente el ciclo de vida de los planes (servicios) 
 * ofrecidos en la base de datos, incluyendo operaciones CRUD y paginación.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    
    // Aquí podrías crear métodos personalizados, por ejemplo:
    // List<Plan> findByNombreContaining(String nombre);
}