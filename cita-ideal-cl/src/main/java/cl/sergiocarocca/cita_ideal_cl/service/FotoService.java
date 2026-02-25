package cl.sergiocarocca.cita_ideal_cl.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Foto;
import cl.sergiocarocca.cita_ideal_cl.repository.FotoRepository;

/**
 * Servicio encargado de la lógica de negocio para la gestión de fotografías.
 * Coordina tanto la persistencia de los metadatos en la base de datos como
 * la manipulación de archivos físicos en el sistema de archivos del servidor.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Service
public class FotoService {

    private final FotoRepository fotoRepository;
    
    /** Ruta relativa al proyecto donde se almacenan las imágenes de la galería. */
    private final String carpetaRelativa = "src/main/resources/static/assets/img/galeria/";

    /**
     * Constructor para la inyección de dependencias.
     * * @param fotoRepository Repositorio para la gestión de la tabla de fotos.
     */
    public FotoService(FotoRepository fotoRepository) {
        this.fotoRepository = fotoRepository;
    }

    /**
     * Recupera todas las fotos de la galería ordenadas por fecha de carga de manera descendente.
     * * @return Lista de objetos {@link Foto} ordenados cronológicamente.
     */
    public List<Foto> listarTodas() {
        return fotoRepository.findAllByOrderByFechaCargaDesc();
    }

    /**
     * Guarda el registro de una nueva fotografía en la base de datos.
     * * @param foto La entidad foto con la información del archivo ya procesado.
     */
    public void guardar(Foto foto) {
        fotoRepository.save(foto);
    }

    /**
     * Realiza una eliminación integral de una fotografía.
     * Este proceso es crítico ya que intenta eliminar primero el archivo físico del disco 
     * para evitar la acumulación de archivos basura y, posteriormente, remueve el 
     * registro de la base de datos.
     * * @param id Identificador único de la foto que se desea eliminar.
     */
    public void eliminarFotoCompleta(Long id) {
        // 1. Buscamos la foto en la DB para conocer el nombre exacto del archivo
        Optional<Foto> fotoOpt = fotoRepository.findById(id);
        
        if (fotoOpt.isPresent()) {
            Foto foto = fotoOpt.get();
            String nombreArchivo = foto.getArchivo();

            try {
                // 2. Intentar borrar el archivo físico en el servidor
                Path ruta = Paths.get(carpetaRelativa).toAbsolutePath().resolve(nombreArchivo);
                Files.deleteIfExists(ruta);
                
                // 3. Si no hubo error en la E/S, procedemos a borrar el registro de la DB
                fotoRepository.deleteById(id);
                
            } catch (IOException e) {
                // Registro del error en consola si falla el acceso al sistema de archivos
                e.printStackTrace();
            }
        }
    }
}