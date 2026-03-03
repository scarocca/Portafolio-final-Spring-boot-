package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Foto;
import cl.sergiocarocca.cita_ideal_cl.service.FotoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Controlador administrativo para la gestión de la galería de imágenes.
 * Proporciona endpoints para subir nuevas fotografías, eliminarlas del sistema
 * y visualizar el listado de archivos multimedia gestionados. * @author Sergio
 * Carocca
 * 
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/galeria")
public class AdminGaleriaController {

	@Autowired
	private FotoService fotoService;

	/**
	 * Muestra la interfaz del formulario para la carga de nuevas imágenes.
	 * * @return El nombre de la plantilla HTML para el formulario de subida.
	 */
	@GetMapping("/nuevo")
	public String formularioSubida() {
		return "admin/galeria-subir";
	}

	/**
	 * Procesa la subida de una imagen, genera un identificador único para el
	 * archivo, lo almacena en el sistema de archivos local y registra la
	 * información en la base de datos. * @param titulo El título descriptivo de la
	 * fotografía.
	 * 
	 * @param archivo El objeto MultipartFile que contiene los datos binarios de la
	 *                imagen.
	 * @param flash   Atributos para mensajes de retroalimentación (éxito/error)
	 *                tras la redirección.
	 * @return Redirección a la vista de gestión o al formulario en caso de error.
	 */
	@PostMapping("/guardar")
	public String guardarFoto(@RequestParam String titulo,
			@RequestParam(value = "archivoImagen", required = false) MultipartFile archivo,
			@RequestParam(value = "urlImagen", required = false) String urlImagen, RedirectAttributes flash) {

		// 1. Validación inicial: ¿Viene al menos una de las dos opciones?
		boolean tieneArchivo = (archivo != null && !archivo.isEmpty());
		boolean tieneUrl = (urlImagen != null && !urlImagen.isBlank());

		if (!tieneArchivo && !tieneUrl) {
			flash.addFlashAttribute("error", "Debes seleccionar un archivo o proporcionar una URL.");
			return "redirect:/admin/galeria/nuevo";
		}

		try {
			Foto nuevaFoto = new Foto();
			nuevaFoto.setTitulo(titulo);

			// 2. Lógica de Decisión
			if (tieneUrl) {
				// CASO A: Es una URL externa
				nuevaFoto.setArchivo(urlImagen);
			} else {
				// CASO B: Es un archivo físico (tu lógica original)
				String carpetaRelativa = "src/main/resources/static/assets/img/galeria/";
				String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();

				Path rutaAbsoluta = Paths.get(carpetaRelativa).toAbsolutePath();

				if (!Files.exists(rutaAbsoluta)) {
					Files.createDirectories(rutaAbsoluta);
				}

				byte[] bytes = archivo.getBytes();
				Path rutaArchivoFinal = rutaAbsoluta.resolve(nombreUnico);
				Files.write(rutaArchivoFinal, bytes);

				nuevaFoto.setArchivo(nombreUnico);
			}

			// 3. Guardar en Base de Datos
			fotoService.guardar(nuevaFoto);
			flash.addFlashAttribute("success", "¡Imagen guardada con éxito!");

		} catch (IOException e) {
			e.printStackTrace();
			flash.addFlashAttribute("error", "Error crítico al guardar: " + e.getMessage());
			return "redirect:/admin/galeria/nuevo";
		}

		return "redirect:/admin/galeria/gestion";
	}

	/**
	 * Elimina una fotografía tanto del registro en la base de datos como del
	 * almacenamiento físico. * @param id El identificador único de la foto a
	 * eliminar.
	 * 
	 * @param flash Atributos para enviar mensajes de confirmación de eliminación.
	 * @return Redirección a la lista de gestión de galería.
	 */
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
		fotoService.eliminarFotoCompleta(id);
		flash.addFlashAttribute("success", "La foto y su archivo han sido eliminados.");
		return "redirect:/admin/galeria/gestion";
	}

	/**
	 * Carga y muestra el panel de gestión con todas las imágenes registradas.
	 * * @param model Objeto para inyectar la lista de fotos y estadísticas en la
	 * vista.
	 * 
	 * @return El nombre de la plantilla HTML para la gestión de la galería.
	 */
	@GetMapping("/gestion")
	public String gestionarGaleria(Model model) {
		// Reutilizamos el servicio para traer todas las fotos
		model.addAttribute("fotos", fotoService.listarTodas());
		// También pasamos el total para el contador del Sidebar si lo usas
		model.addAttribute("totalFotos", fotoService.listarTodas().size());
		return "admin/galeria-gestion";
	}
}