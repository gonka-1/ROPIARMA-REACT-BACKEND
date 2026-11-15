package backend.ropiarma.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ropiarma.models.entities.Carrito;
import backend.ropiarma.models.entities.Usuario;
import backend.ropiarma.repository.CarritoRepository;
import backend.ropiarma.repository.DetalleCarritoRepository;
import backend.ropiarma.repository.UsuarioRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Carrito> listar() {
        return carritoRepository.findAll();
    }

    // devuelve null si no existe
    public Carrito buscarPorId(int id) {
        return carritoRepository.findById(id).orElse(null);
    }

    public List<Carrito> listarPorUsuario(int idUsuario) {
        return carritoRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // devuelve null si el usuario no existe
    public Carrito crear(int idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return null;
        }
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setFechaCreacion(LocalDate.now());
        return carritoRepository.save(carrito);
    }

    // devuelve el ultimo carrito del usuario; si no tiene, crea uno. null si el usuario no existe
    public Carrito obtenerOCrear(int idUsuario) {
        List<Carrito> carritos = carritoRepository.findByUsuarioIdUsuario(idUsuario);
        if (!carritos.isEmpty()) {
            return carritos.get(carritos.size() - 1);
        }
        return crear(idUsuario);
    }

    // elimina primero los detalles para no chocar con la clave foranea
    // devuelve false si no existe
    @Transactional
    public boolean eliminar(int id) {
        if (!carritoRepository.existsById(id)) {
            return false;
        }
        detalleCarritoRepository.deleteByCarritoIdCarrito(id);
        carritoRepository.deleteById(id);
        return true;
    }
}