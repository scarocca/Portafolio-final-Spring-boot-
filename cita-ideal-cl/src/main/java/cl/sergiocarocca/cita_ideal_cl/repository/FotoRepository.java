package cl.sergiocarocca.cita_ideal_cl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.sergiocarocca.cita_ideal_cl.entity.Foto;
import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Foto}.
 * Encapsula la lógica de persistencia para la gestión de imágenes de la galería,
 * permitiendo realizar operaciones CRUD estándar y consultas personalizadas
 * sobre los metadatos de los archivos multimedia.
 * * @author Sergio Carocca
 * @version 1.0
 */
public interface FotoRepository extends JpaRepository<Foto, Long> {

    /**
     * Recupera el listado completo de fotografías almacenadas en el sistema,
     * ordenadas cronológicamente de forma descendente (las más recientes primero).
     * Este método utiliza la convención de nombres de Spring Data JPA para 
     * generar la consulta SQL automáticamente.
     * * @return Una lista de {@link Foto} ordenada por fecha de carga de reciente a antigua.
     */
    List<Foto> findAllByOrderByFechaCargaDesc();
}