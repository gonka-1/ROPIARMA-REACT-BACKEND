package backend.ropiarma.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.ropiarma.models.entities.Resena;
import backend.ropiarma.models.entities.Usuario;
import backend.ropiarma.repository.ResenaRepository;
import backend.ropiarma.repository.UsuarioRepository;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Resena> listar() {
        return resenaRepository.findAll();
    }

    // devuelve null si no existe
    public Resena buscarPorId(int id) {
        return resenaRepository.findById(id).orElse(null);
    }

    public List<Resena> listarPorUsuario(int idUsuario) {
        return resenaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // devuelve null si el usuario no existe o la calificacion no esta entre 1 y 5
    public Resena crear(int idUsuario, int calificacion, String comentario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null || !calificacionValida(calificacion)) {
            return null;
        }
        Resena resena = new Resena();
        resena.setUsuario(usuario);
        resena.setCalificacion(calificacion);
        resena.setComentario(comentario);
        resena.setFecha(LocalDate.now());
        return resenaRepository.save(resena);
    }

    // devuelve null si no existe, no es del usuario o la calificacion es invalida
    public Resena actualizar(int idResena, int idUsuario, int calificacion, String comentario) {
        Resena resena = resenaRepository.findById(idResena).orElse(null);
        if (resena == null
                || resena.getUsuario().getIdUsuario() != idUsuario
                || !calificacionValida(calificacion)) {
            return null;
        }
        resena.setCalificacion(calificacion);
        resena.setComentario(comentario);
        resena.setFecha(LocalDate.now());
        return resenaRepository.save(resena);
    }

    // devuelve false si no existe o no es del usuario
    public boolean eliminar(int idResena, int idUsuario) {
        Resena resena = resenaRepository.findById(idResena).orElse(null);
        if (resena == null || resena.getUsuario().getIdUsuario() != idUsuario) {
            return false;
        }
        resenaRepository.deleteById(idResena);
        return true;
    }

    // promedio de todas las calificaciones (0 si no hay reseñas)
    public double promedioCalificacion() {
        List<Resena> resenas = resenaRepository.findAll();
        if (resenas.isEmpty()) {
            return 0;
        }
        double suma = 0;
        for (Resena r : resenas) {
            suma += r.getCalificacion();
        }
        return suma / resenas.size();
    }

    private boolean calificacionValida(int calificacion) {
        return calificacion >= 1 && calificacion <= 5;
    }
}