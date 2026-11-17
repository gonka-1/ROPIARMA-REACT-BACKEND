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

import backend.ropiarma.models.entities.Resena;
import backend.ropiarma.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/resenas")
@RestController
@Tag(name = "Resenas", description = "Resenas y calificaciones de los usuarios")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Operation(summary = "Lista todas las resenas")
    @GetMapping
    public ResponseEntity<List<Resena>> listar() {
        return ResponseEntity.ok(resenaService.listar());
    }

    @Operation(summary = "Obtiene una resena por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(@PathVariable int id) {
        Resena resena = resenaService.buscarPorId(id);
        if (resena == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resena);
    }

    @Operation(summary = "Lista las resenas de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Resena>> listarPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(resenaService.listarPorUsuario(idUsuario));
    }

    @Operation(summary = "Promedio de calificaciones de todas las resenas")
    @GetMapping("/promedio")
    public ResponseEntity<Map<String, Double>> promedio() {
        return ResponseEntity.ok(Map.of("promedio", resenaService.promedioCalificacion()));
    }

    // body: {"calificacion":5,"comentario":"..."}
    @Operation(summary = "Crea una resena de un usuario")
    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> crear(@PathVariable int idUsuario, @RequestBody Resena datos) {
        Resena resena = resenaService.crear(idUsuario, datos.getCalificacion(), datos.getComentario());
        if (resena == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "El usuario no existe o la calificacion no esta entre 1 y 5"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resena);
    }

    // solo el usuario dueño de la resena puede modificarla
    @Operation(summary = "Actualiza una resena del usuario")
    @PutMapping("/{id}/usuario/{idUsuario}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @PathVariable int idUsuario,
                                        @RequestBody Resena datos) {
        if (resenaService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Resena resena = resenaService.actualizar(id, idUsuario, datos.getCalificacion(), datos.getComentario());
        if (resena == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "La resena no es del usuario o la calificacion no esta entre 1 y 5"));
        }
        return ResponseEntity.ok(resena);
    }

    @Operation(summary = "Elimina una resena del usuario")
    @DeleteMapping("/{id}/usuario/{idUsuario}")
    public ResponseEntity<?> eliminar(@PathVariable int id, @PathVariable int idUsuario) {
        if (resenaService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (!resenaService.eliminar(id, idUsuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", "La resena no es del usuario"));
        }
        return ResponseEntity.noContent().build();
    }
}
