package cl.sergiocarocca.cita_ideal_cl.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.repository.UsuarioRepository;

/**
 * Servicio de autenticación personalizado que implementa {@link UserDetailsService}.
 * Esta clase es el núcleo de la seguridad, ya que permite a Spring Security 
 * verificar las credenciales de un usuario consultando directamente la base de datos 
 * y cargando sus permisos (autoridades).
 * * @author Sergio Carocca
 * @version 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    /**
     * Localiza al usuario basado en su nombre de usuario (email) y construye un 
     * objeto {@link UserDetails} compatible con el framework de seguridad.
     * * @param username El nombre de usuario que intenta iniciar sesión.
     * @return Una instancia de {@link UserDetails} que contiene el username, password y roles.
     * @throws UsernameNotFoundException Si el usuario no existe en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscamos el usuario en la BD por su nombre de usuario
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró una cuenta con el correo: " + username));

        // 2. Convertimos tus Roles (Entidades) en GrantedAuthority (lo que entiende Spring)
        // Usamos Java Streams para transformar cada Rol en una SimpleGrantedAuthority
        List<GrantedAuthority> autoridades = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList());

        // 3. Retornamos un objeto User de Spring Security (clase estándar del framework)
        // Este objeto es el que Spring guardará en el contexto de seguridad (SecurityContext)
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                autoridades
        );
    }
}