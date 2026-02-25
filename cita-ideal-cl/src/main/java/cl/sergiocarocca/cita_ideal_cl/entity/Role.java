package cl.sergiocarocca.cita_ideal_cl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa los privilegios o perfiles de acceso dentro del sistema.
 * Define los niveles de autoridad que puede poseer un usuario para interactuar
 * con los diferentes endpoints protegidos por la configuración de seguridad.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Entity
@Table(name = "roles")
public class Role {

    /** Identificador único autoincremental del rol. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** * Nombre descriptivo del rol. 
     * Debe seguir la convención de Spring Security utilizando el prefijo "ROLE_" 
     * (ejemplo: "ROLE_ADMIN", "ROLE_USER"). Es un campo único y obligatorio.
     */
    @Column(unique = true, nullable = false)
    private String nombre;

    /**
     * Obtiene el identificador del rol.
     * @return El ID numérico.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define el identificador del rol.
     * @param id El nuevo ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del rol (autoridad).
     * @return El nombre del rol con prefijo ROLE_.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el nombre del rol.
     * @param nombre El nombre de la autoridad (ej: ROLE_ADMIN).
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}