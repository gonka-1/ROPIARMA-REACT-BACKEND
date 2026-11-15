package backend.ropiarma.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ropiarma.models.entities.Boleta;
import backend.ropiarma.models.entities.DetalleBoleta;
import backend.ropiarma.models.entities.DetalleCarrito;
import backend.ropiarma.models.entities.Producto;
import backend.ropiarma.repository.BoletaRepository;
import backend.ropiarma.repository.CarritoRepository;
import backend.ropiarma.repository.DetalleBoletaRepository;
import backend.ropiarma.repository.DetalleCarritoRepository;
import backend.ropiarma.repository.ProductoRepository;
import backend.ropiarma.models.entities.Carrito;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Boleta> listar() {
        return boletaRepository.findAll();
    }

    // devuelve null si no existe
    public Boleta buscarPorId(int id) {
        return boletaRepository.findById(id).orElse(null);
    }

    public List<Boleta> listarPorUsuario(int idUsuario) {
        return boletaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // convierte el carrito en boleta: valida stock, descuenta stock, crea detalles y vacia el carrito
    // devuelve null si el carrito no existe, esta vacio o algun producto no tiene stock suficiente
    @Transactional
    public Boleta generarDesdeCarrito(int idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito).orElse(null);
        if (carrito == null) {
            return null;
        }

        List<DetalleCarrito> items = detalleCarritoRepository.findByCarritoIdCarrito(idCarrito);
        if (items.isEmpty()) {
            return null;
        }

        // 1. validar todo el stock antes de modificar nada
        for (DetalleCarrito item : items) {
            if (item.getCantidad() > item.getProducto().getStock()) {
                return null;
            }
        }

        // 2. crear la boleta
        double total = 0;
        for (DetalleCarrito item : items) {
            total += item.getCantidad() * item.getProducto().getPrecio();
        }
        Boleta boleta = new Boleta();
        boleta.setUsuario(carrito.getUsuario());
        boleta.setFecha(LocalDate.now());
        boleta.setTotal(total);
        boleta.setEstado("PENDIENTE");
        boleta = boletaRepository.save(boleta);

        // 3. crear detalles y descontar stock
        for (DetalleCarrito item : items) {
            Producto producto = item.getProducto();

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalleBoletaRepository.save(detalle);

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        // 4. vaciar el carrito
        detalleCarritoRepository.deleteByCarritoIdCarrito(idCarrito);

        return boleta;
    }

    // devuelve null si no existe
    public Boleta actualizarEstado(int id, String estado) {
        Boleta boleta = boletaRepository.findById(id).orElse(null);
        if (boleta == null) {
            return null;
        }
        boleta.setEstado(estado);
        return boletaRepository.save(boleta);
    }

    // marca la boleta como CANCELADA y repone el stock
    // devuelve null si no existe o ya estaba cancelada
    @Transactional
    public Boleta cancelar(int id) {
        Boleta boleta = boletaRepository.findById(id).orElse(null);
        if (boleta == null || "CANCELADA".equals(boleta.getEstado())) {
            return null;
        }
        for (DetalleBoleta d : detalleBoletaRepository.findByBoletaIdBoleta(id)) {
            Producto producto = d.getProducto();
            producto.setStock(producto.getStock() + d.getCantidad());
            productoRepository.save(producto);
        }
        boleta.setEstado("CANCELADA");
        return boletaRepository.save(boleta);
    }
}