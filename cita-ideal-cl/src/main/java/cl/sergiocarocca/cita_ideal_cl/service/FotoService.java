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
import jakarta.transaction.Transactional;

/**
 * Servicio encargado de la lógica de negocio para la gestión de fotografías.
 * Coordina tanto la persistencia de los metadatos en la base de datos como la
 * manipulación de archivos físicos en el sistema de archivos del servidor.
 * * @author Sergio Carocca
 * 
 * @version 1.0
 */
@Service
public class FotoService {

	private final FotoRepository fotoRepository;

	/** Ruta relativa al proyecto donde se almacenan las imágenes de la galería. */
	private final String carpetaRelativa = "src/main/resources/static/assets/img/galeria/";

	/**
	 * Constructor para la inyección de dependencias. * @param fotoRepository
	 * Repositorio para la gestión de la tabla de fotos.
	 */
	public FotoService(FotoRepository fotoRepository) {
		this.fotoRepository = fotoRepository;
	}

	/**
	 * Recupera todas las fotos de la galería ordenadas por fecha de carga de manera
	 * descendente. * @return Lista de objetos {@link Foto} ordenados
	 * cronológicamente.
	 */
	public List<Foto> listarTodas() {
		return fotoRepository.findAllByOrderByFechaCargaDesc();
	}

	/**
	 * Guarda el registro de una nueva fotografía en la base de datos. * @param foto
	 * La entidad foto con la información del archivo ya procesado.
	 */
	public void guardar(Foto foto) {
		fotoRepository.save(foto);
	}

	/**
	 * Realiza una eliminación integral de una fotografía. Este proceso es crítico
	 * ya que intenta eliminar primero el archivo físico del disco para evitar la
	 * acumulación de archivos basura y, posteriormente, remueve el registro de la
	 * base de datos. * @param id Identificador único de la foto que se desea
	 * eliminar.
	 */
	@Transactional
	public void eliminarFotoCompleta(Long id) {
		Foto foto = fotoRepository.findById(id).orElse(null);

		if (foto != null) {
			String nombreArchivo = foto.getArchivo();

			// 1. Solo intentamos borrar del disco si NO es una URL externa
			if (nombreArchivo != null && !nombreArchivo.startsWith("http")) {
				try {
					String carpetaRelativa = "src/main/resources/static/assets/img/galeria/";
					Path rutaAbsoluta = Paths.get(carpetaRelativa).toAbsolutePath().resolve(nombreArchivo);

					if (Files.exists(rutaAbsoluta)) {
						Files.delete(rutaAbsoluta);
						System.out.println("Archivo físico eliminado: " + nombreArchivo);
					}
				} catch (IOException e) {
					System.err.println("Error al eliminar archivo físico: " + e.getMessage());
					// No lanzamos excepción para que al menos se borre de la DB
				}
			} else {
				System.out.println("Es una URL externa, se omite la eliminación física.");
			}

			// 2. Borrar el registro de la Base de Datos siempre
			fotoRepository.deleteById(id);
		}
	}
}