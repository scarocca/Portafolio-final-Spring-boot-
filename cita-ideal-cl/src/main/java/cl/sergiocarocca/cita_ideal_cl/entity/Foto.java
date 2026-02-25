package cl.sergiocarocca.cita_ideal_cl.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una imagen dentro de la galería del sistema.
 * Se utiliza para gestionar el catálogo visual de la plataforma, almacenando
 * los metadatos de la imagen y la referencia al archivo físico.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Entity
@Table(name = "fotos")
public class Foto {

    /** Identificador único autoincremental de la fotografía. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Título descriptivo o pie de foto para mostrar en la interfaz. */
    private String titulo;

    /** * Nombre del archivo físico almacenado en el servidor (ej: "uuid_imagen.jpg").
     * No almacena el contenido binario, solo la ruta/nombre referencial.
     */
    private String archivo; 

    /** Fecha y hora exacta en la que se subió la imagen al sistema. */
    private LocalDateTime fechaCarga;
    
    /**
     * Constructor por defecto requerido por JPA para la instanciación de la entidad.
     */
    public Foto() {
        super();
    }
    
    /**
     * Constructor con todos los campos para la creación manual de instancias.
     * * @param id Identificador único.
     * @param titulo Título de la imagen.
     * @param archivo Nombre del archivo en disco.
     * @param fechaCarga Fecha de subida.
     */
    public Foto(Long id, String titulo, String archivo, LocalDateTime fechaCarga) {
        super();
        this.id = id;
        this.titulo = titulo;
        this.archivo = archivo;
        this.fechaCarga = fechaCarga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    /**
     * Método de ciclo de vida de JPA.
     * Asigna automáticamente la marca de tiempo actual antes de persistir el objeto.
     */
    @PrePersist
    protected void onCreate() {
        this.fechaCarga = LocalDateTime.now();
    }
}