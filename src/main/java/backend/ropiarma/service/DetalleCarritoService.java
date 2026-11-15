package backend.ropiarma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ropiarma.models.entities.Carrito;
import backend.ropiarma.models.entities.DetalleCarrito;
import backend.ropiarma.models.entities.Producto;
import backend.ropiarma.repository.CarritoRepository;
import backend.ropiarma.repository.DetalleCarritoRepository;
import backend.ropiarma.repository.ProductoRepository;

@Service
public class DetalleCarritoService {

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<DetalleCarrito> listarPorCarrito(int idCarrito) {
        return detalleCarritoRepository.findByCarritoIdCarrito(idCarrito);
    }

    // devuelve null si no existe
    public DetalleCarrito buscarPorId(int id) {
        return detalleCarritoRepository.findById(id).orElse(null);
    }

    // si el producto ya esta en el carrito, suma la cantidad
    // devuelve null si el carrito o producto no existen, la cantidad es invalida o no hay stock suficiente
    public DetalleCarrito agregarProducto(int idCarrito, int idProducto, int cantidad) {
        if (cantidad <= 0) {
            return null;
        }
        Carrito carrito = carritoRepository.findById(idCarrito).orElse(null);
        Producto producto = productoRepository.findById(idProducto).orElse(null);
        if (carrito == null || producto == null) {
            return null;
        }

        DetalleCarrito detalle = detalleCarritoRepository
                .findByCarritoIdCarritoAndProductoIdProducto(idCarrito, idProducto)
                .orElse(null);

        int cantidadFinal = cantidad + (detalle == null ? 0 : detalle.getCantidad());
        if (cantidadFinal > producto.getStock()) {
            return null;
        }

        if (detalle == null) {
            detalle = new DetalleCarrito();
            detalle.setCarrito(carrito);
            detalle.setProducto(producto);
        }
        detalle.setCantidad(cantidadFinal);
        detalle.setPrecioUnitario(producto.getPrecio());
        return detalleCarritoRepository.save(detalle);
    }

    // devuelve null si no existe, la cantidad es invalida o supera el stock
    public DetalleCarrito actualizarCantidad(int idDetalle, int cantidad) {
        DetalleCarrito detalle = detalleCarritoRepository.findById(idDetalle).orElse(null);
        if (detalle == null || cantidad <= 0 || cantidad > detalle.getProducto().getStock()) {
            return null;
        }
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(detalle.getProducto().getPrecio());
        return detalleCarritoRepository.save(detalle);
    }

    // devuelve false si no existe
    public boolean eliminar(int idDetalle) {
        if (!detalleCarritoRepository.existsById(idDetalle)) {
            return false;
        }
        detalleCarritoRepository.deleteById(idDetalle);
        return true;
    }

    @Transactional
    public void vaciarCarrito(int idCarrito) {
        detalleCarritoRepository.deleteByCarritoIdCarrito(idCarrito);
    }

    public double calcularTotal(int idCarrito) {
        double total = 0;
        for (DetalleCarrito d : detalleCarritoRepository.findByCarritoIdCarrito(idCarrito)) {
            total += d.getCantidad() * d.getPrecioUnitario();
        }
        return total;
    }
}