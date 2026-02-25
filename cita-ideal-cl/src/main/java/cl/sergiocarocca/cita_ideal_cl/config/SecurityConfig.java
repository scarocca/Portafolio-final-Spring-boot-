package cl.sergiocarocca.cita_ideal_cl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import cl.sergiocarocca.cita_ideal_cl.service.CustomUserDetailsService;

/**
 * Configuración de seguridad principal de la aplicación.
 * Define las políticas de autenticación y autorización, la gestión de sesiones,
 * la encriptación de contraseñas y el acceso a los diferentes recursos del sistema.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Define la cadena de filtros de seguridad (Security Filter Chain).
     * Configura el acceso a rutas públicas (registro, galería, contacto), recursos estáticos,
     * y restringe el acceso a las funciones administrativas exclusivamente para el rol "ADMIN".
     * * @param http Objeto HttpSecurity para configurar la seguridad web.
     * @return El filtro de seguridad configurado.
     * @throws Exception Si ocurre un error durante la configuración de los filtros.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas accesibles para todo el público
                .requestMatchers("/", "/index", "/galeria", "/login", "/registro", "/contacto/**", "/productos").permitAll()    
                // Recursos estáticos (CSS, JS, Imágenes)
                .requestMatchers("/assets/**").permitAll()
                // Restricción para el panel de administración
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .userDetailsService(userDetailsService)
            .formLogin(form -> form
                .loginPage("/login")            
                .defaultSuccessUrl("/productos", true) // Redirección tras login exitoso
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") 
                .permitAll()
            );

        return http.build();
    }
    
    /**
     * Expone el AuthenticationManager de Spring Security para ser utilizado en el
     * proceso de validación de credenciales.
     * * @param authConfig Configuración de autenticación proporcionada por Spring.
     * @return El administrador de autenticación configurado.
     * @throws Exception En caso de error al obtener el manager.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Define el algoritmo de hash para las contraseñas.
     * Utiliza BCrypt, un estándar de la industria que incluye un salt aleatorio para
     * proteger las credenciales contra ataques de fuerza bruta.
     * * @return Una instancia de BCryptPasswordEncoder.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}