package cl.sergiocarocca.cita_ideal_cl.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



import cl.sergiocarocca.cita_ideal_cl.entity.Role;
import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;

import cl.sergiocarocca.cita_ideal_cl.repository.RoleRepository;
import cl.sergiocarocca.cita_ideal_cl.repository.UsuarioRepository;



@Component
public class DataInitializer implements CommandLineRunner {

    
	@Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

	@Override
    public void run(String... args) throws Exception {
        
        // 1. CREACIÓN DE ROLES (Si no existen)
        if (roleRepository.findByNombre("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setNombre("ROLE_USER");
            roleRepository.save(userRole);
            System.out.println("Rol ROLE_USER creado exitosamente.");
        }

        if (roleRepository.findByNombre("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setNombre("ROLE_ADMIN");
            roleRepository.save(adminRole);
            System.out.println("Rol ROLE_ADMIN creado exitosamente.");
        }

        // 2. CREACIÓN DE USUARIO ADMINISTRADOR INICIAL (Opcional pero recomendado)
        // Esto te permite entrar a la sección de admin de inmediato
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            // Seteamos la contraseña "admin123" cifrada
            admin.setPassword(passwordEncoder.encode("admin123"));

            // Buscamos el objeto rol que acabamos de asegurar que existe
            Role adminRole = roleRepository.findByNombre("ROLE_ADMIN").get();
            
            // Usamos el método añadirRol que creamos para evitar errores de Set
            admin.añadirRol(adminRole);
            
            usuarioRepository.save(admin);
            System.out.println("Usuario 'admin' creado con contraseña 'admin123'.");
        }
    }
}