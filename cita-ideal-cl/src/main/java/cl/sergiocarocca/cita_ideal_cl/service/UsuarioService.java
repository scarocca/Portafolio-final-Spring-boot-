	package cl.sergiocarocca.cita_ideal_cl.service;
	
	import java.util.List;
	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
	import org.springframework.stereotype.Service;
	import cl.sergiocarocca.cita_ideal_cl.entity.Role;
	import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
	import cl.sergiocarocca.cita_ideal_cl.repository.RoleRepository;
	import cl.sergiocarocca.cita_ideal_cl.repository.UsuarioRepository;
	
	/**
	 * Servicio encargado de la gestión de usuarios y seguridad de acceso.
	 * Proporciona la lógica para el registro de nuevos usuarios, la encriptación
	 * de credenciales y la administración de perfiles dentro del sistema.
	 * * @author Sergio Carocca
	 * @version 1.0
	 */
	@Service
	public class UsuarioService {
	
	    private final UsuarioRepository usuarioRepo;
	    private final RoleRepository roleRepo;
	    private final BCryptPasswordEncoder passwordEncoder;
	
	    /**
	     * Constructor para la inyección de dependencias.
	     * * @param usuarioRepo Repositorio para la persistencia de usuarios.
	     * @param roleRepo Repositorio para la gestión de roles.
	     * @param passwordEncoder Componente para el cifrado seguro de contraseñas.
	     */
	    public UsuarioService(UsuarioRepository usuarioRepo, RoleRepository roleRepo,
	            BCryptPasswordEncoder passwordEncoder) {
	        super();
	        this.usuarioRepo = usuarioRepo;
	        this.roleRepo = roleRepo;
	        this.passwordEncoder = passwordEncoder;
	    }
	
	    /**
	     * Realiza el registro de un nuevo usuario desde el formulario público.
	     * Valida la integridad de la contraseña, aplica un hash de seguridad (BCrypt)
	     * y asigna automáticamente el rol de usuario estándar ("ROLE_USER").
	     * * @param usuario El objeto usuario con los datos capturados en el formulario.
	     * @throws IllegalArgumentException Si las contraseñas ingresadas no coinciden.
	     * @throws RuntimeException Si el rol por defecto no se encuentra en la base de datos.
	     */
	    public void registrarUsuarioPublico(Usuario usuario) {
	        
	    	if (usuarioRepo.existsByEmail(usuario.getEmail())) {
	            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
	        }
	       
	    	// Validación de negocio: Coincidencia de contraseñas
	        if (usuario.getConfirmacionPassword() == null || 
	                !usuario.getPassword().equals(usuario.getConfirmacionPassword())) {
	            throw new IllegalArgumentException("Las contraseñas no coinciden");
	        }
	        usuario.setUsername(usuario.getEmail());
	
	        // 1. Cifrar password utilizando el algoritmo BCrypt
	        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	
	        // 2. Buscar el rol "ROLE_USER" en la BD (Obligatorio para nuevos registros)
	        Role userRole = roleRepo.findByNombre("ROLE_USER")
	                .orElseThrow(() -> new RuntimeException("El rol ROLE_USER no existe en la BD"));
	
	        // 3. Asignar el rol y persistir en la base de datos
	        usuario.añadirRol(userRole);
	        usuarioRepo.save(usuario);
	    }
	    
	    /**
	     * Recupera el listado completo de usuarios registrados para fines administrativos.
	     * * @return Lista de todos los objetos {@link Usuario}.
	     */
	    public List<Usuario> listartodo() {
	        return usuarioRepo.findAll();
	    }
	
	    /**
	     * Elimina a un usuario del sistema basándose en su identificador único.
	     * * @param id El ID del usuario a eliminar.
	     * @throws RuntimeException Si no se encuentra un usuario con el identificador proporcionado.
	     */
	    public void eliminar(Long id) {
	        if (usuarioRepo.existsById(id)) {
	            usuarioRepo.deleteById(id);
	        } else {
	            throw new RuntimeException("Usuario no encontrado con el ID: " + id);
	        }
	    }
	    /**
	     * Busca un usuario en la base de datos a través de su correo electrónico.
	     * Este método es vital para vincular las reservas al usuario logueado en el panel.
	     * * @param email Correo electrónico del usuario.
	     * @return El objeto Usuario encontrado.
	     * @throws RuntimeException Si el usuario no existe.
	     */
	    public Usuario obtenerPorEmail(String email) {
	        return usuarioRepo.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email));
	    }
	}