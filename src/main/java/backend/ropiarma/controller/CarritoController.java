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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ropiarma.models.entities.Carrito;
import backend.ropiarma.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/carritos")
@RestController
@Tag(name = "Carritos", description = "Carritos de compra de los usuarios")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Lista todos los carritos")
    @GetMapping
    public ResponseEntity<List<Carrito>> listar() {
        return ResponseEntity.ok(carritoService.listar());
    }

    @Operation(summary = "Obtiene un carrito por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> buscarPorId(@PathVariable int id) {
        Carrito carrito = carritoService.buscarPorId(id);
        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carrito);
    }

    @Operation(summary = "Lista los carritos de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Carrito>> listarPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(carritoService.listarPorUsuario(idUsuario));
    }

    // el frontend usa este endpoint para obtener el carrito activo del usuario
    @Operation(summary = "Obtiene el carrito actual del usuario (lo crea si no tiene)")
    @GetMapping("/usuario/{idUsuario}/actual")
    public ResponseEntity<?> obtenerOCrear(@PathVariable int idUsuario) {
        Carrito carrito = carritoService.obtenerOCrear(idUsuario);
        if (carrito == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "El usuario no existe"));
        }
        return ResponseEntity.ok(carrito);
    }

    @Operation(summary = "Crea un carrito nuevo para un usuario")
    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> crear(@PathVariable int idUsuario) {
        Carrito carrito = carritoService.crear(idUsuario);
        if (carrito == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "El usuario no existe"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
    }

    @Operation(summary = "Elimina un carrito y sus detalles")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!carritoService.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
