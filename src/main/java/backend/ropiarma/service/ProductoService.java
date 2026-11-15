package backend.ropiarma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.ropiarma.models.entities.Producto;
import backend.ropiarma.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    // devuelve null si no existe
    public Producto buscarPorId(int id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> listarPorCategoria(int idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // devuelve null si no existe
    public Producto actualizar(int id, Producto datos) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return null;
        }
        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        producto.setCategoria(datos.getCategoria());
        return productoRepository.save(producto);
    }

    // devuelve false si no existe
    public boolean eliminar(int id) {
        if (!productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }

    // devuelve false si no hay stock suficiente o el producto no existe
    public boolean descontarStock(int id, int cantidad) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null || producto.getStock() < cantidad) {
            return false;
        }
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
        return true;
    }
}