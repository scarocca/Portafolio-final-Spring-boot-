package cl.sergiocarocca.cita_ideal_cl.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una consulta de contacto enviada por un usuario.
 * Almacena la información de contacto y el mensaje, permitiendo opcionalmente
 * vincular la inquietud a un plan de servicio específico.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Entity
@Table(name = "consultas")
public class Consulta {

    /** Identificador único autoincremental de la consulta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo del usuario que realiza la consulta. */
    private String nombre;

    /** Dirección de correo electrónico de contacto del remitente. */
    private String email;
    
    /** Contenido detallado del mensaje enviado por el usuario. */
    @Column(columnDefinition = "TEXT")
    private String mensaje;

    /** Fecha y hora exacta en la que se registró la consulta en el sistema. */
    private LocalDateTime fechaEnvio;

    /** * Relación con el plan por el cual se está consultando.
     * Esta asociación es opcional (puede ser nula si la consulta es general).
     */
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    
    /** Relación con el usuario que envió la consulta (para el panel de cliente) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @Column(columnDefinition = "TEXT")
    private String respuesta;

    private LocalDateTime fechaRespuesta;
    
 // Dentro de Consulta.java
    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensajeChat> mensajes = new ArrayList<>();

    /**
     * Método de ciclo de vida de JPA.
     * Se ejecuta automáticamente antes de insertar el registro en la base de datos
     * para asignar la fecha y hora actual al campo fechaEnvio.
     */
    @PrePersist
    protected void onCreate() {
        fechaEnvio = LocalDateTime.now();
    }

    // --- Métodos Getter y Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public LocalDateTime getFechaRespuesta() {
		return fechaRespuesta;
	}

	public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	public List<MensajeChat> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<MensajeChat> mensajes) {
		this.mensajes = mensajes;
	}
	
    
}