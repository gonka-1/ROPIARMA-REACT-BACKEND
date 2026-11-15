package backend.ropiarma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.ropiarma.models.entities.Ubicacion;
import backend.ropiarma.models.entities.Usuario;
import backend.ropiarma.repository.UbicacionRepository;
import backend.ropiarma.repository.UsuarioRepository;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Ubicacion> listar() {
        return ubicacionRepository.findAll();
    }

    // devuelve null si no existe
    public Ubicacion buscarPorId(int id) {
        return ubicacionRepository.findById(id).orElse(null);
    }

    public List<Ubicacion> listarPorUsuario(int idUsuario) {
        return ubicacionRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // devuelve null si el usuario no existe o el costo de envio es negativo
    public Ubicacion crear(int idUsuario, Ubicacion datos) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null || datos.getCostoEnvio() < 0) {
            return null;
        }
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setUsuario(usuario);
        ubicacion.setRegion(datos.getRegion());
        ubicacion.setComuna(datos.getComuna());
        ubicacion.setDireccion(datos.getDireccion());
        ubicacion.setCostoEnvio(datos.getCostoEnvio());
        return ubicacionRepository.save(ubicacion);
    }

    // devuelve null si no existe, no es del usuario o el costo es negativo
    public Ubicacion actualizar(int idUbicacion, int idUsuario, Ubicacion datos) {
        Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion).orElse(null);
        if (ubicacion == null
                || ubicacion.getUsuario().getIdUsuario() != idUsuario
                || datos.getCostoEnvio() < 0) {
            return null;
        }
        ubicacion.setRegion(datos.getRegion());
        ubicacion.setComuna(datos.getComuna());
        ubicacion.setDireccion(datos.getDireccion());
        ubicacion.setCostoEnvio(datos.getCostoEnvio());
        return ubicacionRepository.save(ubicacion);
    }

    // devuelve false si no existe o no es del usuario
    public boolean eliminar(int idUbicacion, int idUsuario) {
        Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion).orElse(null);
        if (ubicacion == null || ubicacion.getUsuario().getIdUsuario() != idUsuario) {
            return false;
        }
        ubicacionRepository.deleteById(idUbicacion);
        return true;
    }

    // costo de envio de una ubicacion (-1 si no existe)
    public double obtenerCostoEnvio(int idUbicacion) {
        Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion).orElse(null);
        return ubicacion == null ? -1 : ubicacion.getCostoEnvio();
    }
}