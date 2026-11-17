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

import backend.ropiarma.models.entities.Usuario;
import backend.ropiarma.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/usuarios")
@RestController
@Tag(name = "Usuarios", description = "CRUD de usuarios del sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Lista todos los usuarios")
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @Operation(summary = "Obtiene un usuario por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable int id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Obtiene un usuario por su email")
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    // body: {"nombre":"...","apellido":"...","email":"...","password":"...","rol":"..."}
    @Operation(summary = "Crea un usuario")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.guardar(usuario);
        if (nuevo == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "El email ya esta registrado"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // el servicio devuelve null tanto si no existe como si el email ya lo usa otro,
    // por eso primero se revisa si existe para responder 404 o 409
    @Operation(summary = "Actualiza un usuario")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Usuario datos) {
        if (usuarioService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = usuarioService.actualizar(id, datos);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "El email ya lo usa otro usuario"));
        }
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Elimina un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!usuarioService.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
