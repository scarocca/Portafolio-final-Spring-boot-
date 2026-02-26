package cl.sergiocarocca.cita_ideal_cl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;

/**
 * Repositorio de acceso a datos para la entidad {@link Usuario}.
 * Proporciona los métodos necesarios para la gestión de cuentas de usuario y 
 * es el componente principal utilizado por el servicio de autenticación para 
 * localizar credenciales en la base de datos.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Localiza un usuario en la base de datos basándose en su nombre de usuario (username).
     * Este método es crítico para el proceso de Login, ya que permite recuperar
     * la contraseña encriptada y los roles asociados al usuario.
     * * @param username El nombre de usuario o correo electrónico del usuario.
     * @return Un {@link Optional} que contiene el {@link Usuario} si existe, 
     * o un contenedor vacío si el nombre de usuario no se encuentra.
     */
    Optional<Usuario> findByUsername(String username);
    boolean existsByEmail(String email);
}