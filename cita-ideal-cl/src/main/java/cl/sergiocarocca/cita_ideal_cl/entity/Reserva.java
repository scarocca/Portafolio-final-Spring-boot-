package cl.sergiocarocca.cita_ideal_cl.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa la reserva de un servicio por parte de un cliente.
 * Almacena la información de contacto, el plan seleccionado, la fecha de la cita
 * y un código único de seguimiento para la gestión administrativa.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Entity
@Table(name = "reservas")
public class Reserva {

    /** Identificador único autoincremental de la reserva. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** * Relación con el Plan de servicio seleccionado.
     * Se utiliza FetchType.LAZY para optimizar el rendimiento cargando el plan bajo demanda.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    /** Nombre completo del cliente que realiza la reserva. */
    @Column(nullable = false)
    private String nombreCliente;

    /** Correo electrónico de contacto del cliente. */
    @Column(nullable = false)
    private String emailCliente;

    /** Número de teléfono de contacto para coordinar la cita. */
    @Column(nullable = false)
    private String telefonoCliente;

    /** Fecha y hora programada para la prestación del servicio. */
    @Column(nullable = false)
    private LocalDateTime fechaCita;

    /** * Estado actual de la reserva. 
     * Los valores posibles son: PENDIENTE, CONFIRMADA, CANCELADA.
     */
    private String estado = "PENDIENTE"; 
    
    /** * Código alfanumérico único para que el cliente identifique su reserva. 
     */
    private String codigoSeguimiento;

    /**
     * Constructor por defecto requerido por JPA.
     * Inicializa automáticamente un código de seguimiento único basado en UUID.
     */
    public Reserva() {
        // Generamos un código de seguimiento único al azar (primeros 8 caracteres)
        this.codigoSeguimiento = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Constructor con parámetros para facilitar la creación de reservas desde los servicios.
     * * @param plan El servicio seleccionado.
     * @param nombreCliente Nombre del titular.
     * @param emailCliente Correo de contacto.
     * @param telefonoCliente Teléfono de contacto.
     * @param fechaCita Fecha y hora de la cita.
     */
    public Reserva(Plan plan, String nombreCliente, String emailCliente, String telefonoCliente, LocalDateTime fechaCita) {
        this(); // Invoca al constructor vacío para asegurar la generación del código de seguimiento
        this.plan = plan;
        this.nombreCliente = nombreCliente;
        this.emailCliente = emailCliente;
        this.telefonoCliente = telefonoCliente;
        this.fechaCita = fechaCita;
    }

    // --- Métodos Getter y Setter ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public LocalDateTime getFechaCita() { return fechaCita; }
    public void setFechaCita(LocalDateTime fechaCita) { this.fechaCita = fechaCita; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public void setCodigoSeguimiento(String codigoSeguimiento) { this.codigoSeguimiento = codigoSeguimiento; }
}