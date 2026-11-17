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
import org.springframework.web.bind.annotation.RestController;

import backend.ropiarma.models.entities.Ubicacion;
import backend.ropiarma.service.UbicacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/ubicaciones")
@RestController
@Tag(name = "Ubicaciones", description = "Direcciones de despacho de los usuarios")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    @Operation(summary = "Lista todas las ubicaciones")
    @GetMapping
    public ResponseEntity<List<Ubicacion>> listar() {
        return ResponseEntity.ok(ubicacionService.listar());
    }

    @Operation(summary = "Obtiene una ubicacion por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> buscarPorId(@PathVariable int id) {
        Ubicacion ubicacion = ubicacionService.buscarPorId(id);
        if (ubicacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ubicacion);
    }

    @Operation(summary = "Lista las ubicaciones de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Ubicacion>> listarPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(ubicacionService.listarPorUsuario(idUsuario));
    }

    @Operation(summary = "Obtiene el costo de envio de una ubicacion")
    @GetMapping("/{id}/costo-envio")
    public ResponseEntity<Map<String, Double>> obtenerCostoEnvio(@PathVariable int id) {
        double costo = ubicacionService.obtenerCostoEnvio(id);
        if (costo < 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("costoEnvio", costo));
    }

    // body: {"region":"...","comuna":"...","direccion":"...","costoEnvio":3500}
    @Operation(summary = "Crea una ubicacion para un usuario")
    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> crear(@PathVariable int idUsuario, @RequestBody Ubicacion datos) {
        Ubicacion ubicacion = ubicacionService.crear(idUsuario, datos);
        if (ubicacion == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "El usuario no existe o el costo de envio es negativo"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ubicacion);
    }

    // solo el usuario dueño de la ubicacion puede modificarla
    @Operation(summary = "Actualiza una ubicacion del usuario")
    @PutMapping("/{id}/usuario/{idUsuario}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @PathVariable int idUsuario,
                                        @RequestBody Ubicacion datos) {
        if (ubicacionService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Ubicacion ubicacion = ubicacionService.actualizar(id, idUsuario, datos);
        if (ubicacion == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "La ubicacion no es del usuario o el costo de envio es negativo"));
        }
        return ResponseEntity.ok(ubicacion);
    }

    @Operation(summary = "Elimina una ubicacion del usuario")
    @DeleteMapping("/{id}/usuario/{idUsuario}")
    public ResponseEntity<?> eliminar(@PathVariable int id, @PathVariable int idUsuario) {
        if (ubicacionService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (!ubicacionService.eliminar(id, idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", "La ubicacion no es del usuario"));
        }
        return ResponseEntity.noContent().build();
    }
}
