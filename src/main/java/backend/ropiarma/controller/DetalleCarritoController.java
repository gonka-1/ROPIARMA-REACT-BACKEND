package backend.ropiarma.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.ropiarma.models.entities.DetalleCarrito;
import backend.ropiarma.service.DetalleCarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/detalles-carrito")
@RestController
@Tag(name = "Detalles de carrito", description = "Productos agregados a un carrito")
public class DetalleCarritoController {

    @Autowired
    private DetalleCarritoService detalleCarritoService;

    @Operation(summary = "Lista los productos de un carrito")
    @GetMapping("/carrito/{idCarrito}")
    public ResponseEntity<List<DetalleCarrito>> listarPorCarrito(@PathVariable int idCarrito) {
        return ResponseEntity.ok(detalleCarritoService.listarPorCarrito(idCarrito));
    }

    @Operation(summary = "Obtiene un detalle de carrito por su id")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleCarrito> buscarPorId(@PathVariable int id) {
        DetalleCarrito detalle = detalleCarritoService.buscarPorId(id);
        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalle);
    }

    @Operation(summary = "Calcula el total del carrito")
    @GetMapping("/carrito/{idCarrito}/total")
    public ResponseEntity<Map<String, Double>> calcularTotal(@PathVariable int idCarrito) {
        return ResponseEntity.ok(Map.of("total", detalleCarritoService.calcularTotal(idCarrito)));
    }

    // ejemplo: POST /api/v1/detalles-carrito/carrito/1?idProducto=3&cantidad=2
    @Operation(summary = "Agrega un producto al carrito (si ya estaba, suma la cantidad)")
    @PostMapping("/carrito/{idCarrito}")
    public ResponseEntity<?> agregarProducto(@PathVariable int idCarrito,
                                             @RequestParam int idProducto,
                                             @RequestParam int cantidad) {
        DetalleCarrito detalle = detalleCarritoService.agregarProducto(idCarrito, idProducto, cantidad);
        if (detalle == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "Carrito o producto no existe, cantidad invalida o stock insuficiente"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(detalle);
    }

    // ejemplo: PUT /api/v1/detalles-carrito/5?cantidad=4
    @Operation(summary = "Cambia la cantidad de un producto del carrito")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCantidad(@PathVariable int id, @RequestParam int cantidad) {
        if (detalleCarritoService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        DetalleCarrito detalle = detalleCarritoService.actualizarCantidad(id, cantidad);
        if (detalle == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Cantidad invalida o stock insuficiente"));
        }
        return ResponseEntity.ok(detalle);
    }

    @Operation(summary = "Quita un producto del carrito")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!detalleCarritoService.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vacia el carrito")
    @DeleteMapping("/carrito/{idCarrito}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable int idCarrito) {
        detalleCarritoService.vaciarCarrito(idCarrito);
        return ResponseEntity.noContent().build();
    }
}
