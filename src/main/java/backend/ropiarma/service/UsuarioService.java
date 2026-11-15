package backend.ropiarma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.ropiarma.models.entities.Usuario;
import backend.ropiarma.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // devuelve null si no existe
    public Usuario buscarPorId(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // devuelve null si no existe
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    // devuelve null si el email ya esta registrado
    public Usuario guardar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return null;
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("USER");
        }
        return usuarioRepository.save(usuario);
    }

    // devuelve null si no existe o si el nuevo email ya lo usa otro usuario
    public Usuario actualizar(int id, Usuario datos) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return null;
        }
        boolean emailCambio = !usuario.getEmail().equalsIgnoreCase(datos.getEmail());
        if (emailCambio && usuarioRepository.existsByEmail(datos.getEmail())) {
            return null;
        }
        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setEmail(datos.getEmail());
        if (datos.getRol() != null && !datos.getRol().isBlank()) {
            usuario.setRol(datos.getRol());
        }
        // la password solo se cambia si viene una nueva
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(datos.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    // devuelve false si no existe
    public boolean eliminar(int id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById(id);
        return true;
    }
}