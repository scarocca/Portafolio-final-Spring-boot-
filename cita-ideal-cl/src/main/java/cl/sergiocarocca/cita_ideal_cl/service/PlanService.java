package cl.sergiocarocca.cita_ideal_cl.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.repository.PlanRepository;

/**
 * Servicio encargado de la lógica de negocio para la gestión de Planes.
 * Proporciona métodos para el mantenimiento del catálogo de servicios, incluyendo
 * la gestión de archivos multimedia asociados y el control de visibilidad de los planes.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    /** Ruta física donde se almacenan las imágenes de los planes en el servidor. */
    private final String carpetaPlanes = "src/main/resources/static/assets/img/planes/";

    /**
     * Busca un plan específico por su identificador único.
     * * @param id Identificador del plan.
     * @return El objeto {@link Plan} encontrado o null si no existe.
     */
    public Plan buscarPorId(Long id) {
        return planRepository.findById(id).orElse(null);
    }
	
    /**
     * Persiste o actualiza un plan en la base de datos.
     * * @param plan Objeto plan con los datos a guardar.
     */
    public void guardar(Plan plan) {
        planRepository.save(plan);
    }

    /**
     * Elimina un plan de forma integral. 
     * Este método primero intenta borrar el archivo de imagen físico del servidor para 
     * optimizar el almacenamiento y luego elimina el registro de la base de datos.
     * * @param id Identificador del plan a eliminar.
     */
    public void eliminar(Long id) {
        Plan plan = buscarPorId(id);
        
        if (plan != null) {
            // Solo intentamos borrar el archivo si imagenUrl no es nulo ni está vacío
            if (plan.getImagenUrl() != null && !plan.getImagenUrl().isEmpty()) {
                try {
                    Path ruta = Paths.get(carpetaPlanes)
                            .toAbsolutePath()
                            .resolve(plan.getImagenUrl());
                    
                    // deleteIfExists evita excepciones si el archivo ya no estaba presente
                    Files.deleteIfExists(ruta);
                } catch (IOException e) {
                    System.err.println("No se pudo eliminar el archivo, pero seguiremos con la DB: " + e.getMessage());
                }
            }
            
            // Finalmente borramos de la base de datos
            planRepository.deleteById(id);
        }
    }

    /**
     * Recupera todos los planes registrados en el sistema, sin importar su estado.
     * * @return Lista completa de objetos {@link Plan}.
     */
    public List<Plan> listarTodos() {
        return planRepository.findAll();
    }

    /**
     * Desactiva un plan de forma lógica para que no sea visible en el catálogo público,
     * pero manteniendo sus datos en la base de datos.
     * * @param id Identificador del plan a ocultar.
     */
    public void ocultarPlan(Long id) {
        Plan plan = buscarPorId(id);
        if (plan != null) {
            plan.setActivo(false);
            planRepository.save(plan);
        }
    }

    /**
     * Activa un plan previamente oculto para que vuelva a estar disponible en el catálogo público.
     * * @param id Identificador del plan a activar.
     */
    public void activarPlan(Long id) {
        Plan plan = buscarPorId(id);
        if (plan != null) {
            plan.setActivo(true);
            planRepository.save(plan);
        }
    }

    /**
     * Filtra y retorna únicamente los planes que tienen el estado de activo habilitado.
     * Utilizado principalmente para la vista de clientes finales.
     * * @return Lista de planes visibles al público.
     */
    public List<Plan> listarPlanesActivos() {
        return planRepository.findAll().stream()
                             .filter(p -> p.isActivo())
                             .toList();
    }
}