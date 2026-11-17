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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.ropiarma.models.entities.Categoria;
import backend.ropiarma.models.entities.Producto;
import backend.ropiarma.service.CategoriaService;
import backend.ropiarma.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/productos")
@RestController
@Tag(name = "Productos", description = "CRUD de productos de la tienda")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    // si viene ?nombre= busca por nombre, si no lista todos
    @Operation(summary = "Lista todos los productos o busca por nombre")
    @GetMapping
    public ResponseEntity<List<Producto>> listar(@RequestParam(required = false) String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
        }
        return ResponseEntity.ok(productoService.listar());
    }

    @Operation(summary = "Obtiene un producto por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable int id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Lista los productos de una categoria")
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable int idCategoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(idCategoria));
    }

    // body: {"nombre":"...","descripcion":"...","precio":1000,"stock":10,"categoria":{"idCategoria":1}}
    @Operation(summary = "Crea un producto")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        Categoria categoria = obtenerCategoria(producto);
        if (categoria == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "La categoria no existe"));
        }
        producto.setCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(producto));
    }

    @Operation(summary = "Actualiza un producto")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Producto datos) {
        Categoria categoria = obtenerCategoria(datos);
        if (categoria == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "La categoria no existe"));
        }
        datos.setCategoria(categoria);
        Producto producto = productoService.actualizar(id, datos);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Elimina un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!productoService.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // busca la categoria completa a partir del id que viene en el body (null si no viene o no existe)
    private Categoria obtenerCategoria(Producto producto) {
        if (producto.getCategoria() == null) {
            return null;
        }
        return categoriaService.buscarPorId(producto.getCategoria().getIdCategoria());
    }
}
