package cl.sergiocarocca.cita_ideal_cl.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Entidad que representa a los usuarios del sistema.
 * Almacena las credenciales de acceso y gestiona la relación con los roles 
 * asignados, permitiendo la integración con el módulo de seguridad de Spring.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /** Identificador único autoincremental del usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;

    /** Nombre de usuario único utilizado para el inicio de sesión (generalmente el email). */
    @Column(unique = true)
    private String username;

    /** Contraseña encriptada del usuario. */
    private String password;
    
   

    /** * Conjunto de roles asignados al usuario.
     * Se utiliza FetchType.EAGER para cargar los roles inmediatamente junto con el usuario,
     * garantizando que las autoridades estén disponibles para Spring Security.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    /** * Campo auxiliar utilizado exclusivamente para la validación de contraseñas en formularios.
     * La anotación @Transient asegura que este campo no sea persistido en la base de datos.
     */
    @Transient 
    private String confirmacionPassword;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Usuario() {
        super();
    }
    
    /**
     * Constructor con parámetros para facilitar la instanciación completa del usuario.
     * * @param id Identificador único.
     * @param username Nombre de usuario.
     * @param password Contraseña ya procesada/encriptada.
     * @param roles Conjunto inicial de roles.
     */
    public Usuario(Long id,String email, String username, String password, Set<Role> roles) {
        super();
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    /**
     * Agrega un nuevo rol a la colección actual del usuario.
     * * @param role El rol (autoridad) a añadir.
     */
    public void añadirRol(Role role) {
        this.roles.add(role);
    }

    public String getConfirmacionPassword() {
        return confirmacionPassword;
    }

    public void setConfirmacionPassword(String confirmacionPassword) {
        this.confirmacionPassword = confirmacionPassword;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
}