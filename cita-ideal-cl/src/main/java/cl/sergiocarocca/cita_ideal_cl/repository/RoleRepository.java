package cl.sergiocarocca.cita_ideal_cl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergiocarocca.cita_ideal_cl.entity.Role;

/**
 * Repositorio de acceso a datos para la entidad {@link Role}.
 * Proporciona los métodos necesarios para gestionar los roles y permisos del sistema.
 * Es fundamental para la asignación de autoridades durante el proceso de 
 * registro y autenticación de usuarios.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol específico en la base de datos a través de su nombre.
     * Utiliza {@link Optional} como contenedor para facilitar el manejo de casos
     * donde el rol buscado (ej: "ROLE_ADMIN") no se encuentre registrado.
     * * @param nombre El nombre del rol a buscar (debe incluir el prefijo ROLE_).
     * @return Un Optional que contiene el {@link Role} si se encuentra, o un contenedor vacío.
     */
    Optional<Role> findByNombre(String nombre);
}