package backend.ropiarma.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.ropiarma.models.entities.Boleta;
import backend.ropiarma.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/boletas")
@RestController
@Tag(name = "Boletas", description = "Ordenes de compra generadas desde el carrito")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    @Operation(summary = "Lista todas las boletas")
    @GetMapping
    public ResponseEntity<List<Boleta>> listar() {
        return ResponseEntity.ok(boletaService.listar());
    }

    @Operation(summary = "Obtiene una boleta por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Boleta> buscarPorId(@PathVariable int id) {
        Boleta boleta = boletaService.buscarPorId(id);
        if (boleta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boleta);
    }

    @Operation(summary = "Lista las boletas de un usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Boleta>> listarPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(boletaService.listarPorUsuario(idUsuario));
    }

    @Operation(summary = "Genera una boleta a partir del carrito (descuenta stock y vacia el carrito)")
    @PostMapping("/carrito/{idCarrito}")
    public ResponseEntity<?> generarDesdeCarrito(@PathVariable int idCarrito) {
        Boleta boleta = boletaService.generarDesdeCarrito(idCarrito);
        if (boleta == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "El carrito no existe, esta vacio o algun producto no tiene stock suficiente"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(boleta);
    }

    // ejemplo: PUT /api/v1/boletas/1/estado?estado=PAGADA
    @Operation(summary = "Actualiza el estado de una boleta")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Boleta> actualizarEstado(@PathVariable int id, @RequestParam String estado) {
        Boleta boleta = boletaService.actualizarEstado(id, estado);
        if (boleta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boleta);
    }

    // la boleta no se borra de la base de datos, se cancela y se repone el stock
    @Operation(summary = "Cancela una boleta y repone el stock")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable int id) {
        if (boletaService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Boleta boleta = boletaService.cancelar(id);
        if (boleta == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "La boleta ya estaba cancelada"));
        }
        return ResponseEntity.ok(boleta);
    }
}
