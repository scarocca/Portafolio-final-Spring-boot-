package cl.sergiocarocca.cita_ideal_cl.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un plan de servicio o producto ofrecido por la plataforma.
 * Contiene la definición comercial del servicio, incluyendo su costo, descripción 
 * y estado de disponibilidad para el catálogo público.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Entity
public class Plan {

    /** Identificador único autoincremental del plan en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** * Nombre comercial del plan. 
     * Restricción: No puede ser nulo y debe tener entre 5 y 25 caracteres.
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 5, max = 25, message = "El nombre debe tener entre 5 y 25 caracteres")
    private String nombre;

    /** * Descripción detallada de lo que incluye el servicio. 
     * Se almacena como tipo TEXT en la base de datos para soportar contenido extenso.
     */
    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    /** * Costo base del servicio.
     * Utiliza {@link BigDecimal} para evitar errores de precisión decimal.
     * Restricción: Debe ser un valor estrictamente mayor a 0.0.
     */
    @Column(nullable = false)
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioBase;
    
    /** * Indica si el plan está visible en el catálogo público. 
     * Por defecto es true.
     */
    private boolean activo = true;

    /** * Ruta o nombre del archivo de imagen que ilustra el plan. 
     */
    private String imagenUrl;
    
    /**
     * Constructor por defecto requerido por JPA.
     */
    public Plan() {
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    /**
     * Verifica si el plan se encuentra en estado activo.
     * * @return true si el plan es visible, false en caso contrario.
     */
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}