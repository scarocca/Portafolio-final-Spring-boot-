package cl.sergiocarocca.cita_ideal_cl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import cl.sergiocarocca.cita_ideal_cl.entity.Role;
import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.repository.RoleRepository;
import cl.sergiocarocca.cita_ideal_cl.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Asegurar que existan los roles
        Role adminRole = asegurarRol("ROLE_ADMIN");
        asegurarRol("ROLE_USER");

        // 2. Crear el admin si no existe
        if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail(adminEmail);
            admin.setUsername(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.añadirRol(adminRole);
            
            usuarioRepository.save(admin);
            System.out.println(">>> Usuario administrativo creado con: " + adminEmail);
        }
    }

    private Role asegurarRol(String nombre) {
        return roleRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    Role nuevoRol = new Role();
                    nuevoRol.setNombre(nombre);
                    return roleRepository.save(nuevoRol);
                });
    }
}