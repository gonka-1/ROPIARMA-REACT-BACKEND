package backend.ropiarma.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ropiarma.models.entities.DetalleBoleta;
import backend.ropiarma.service.DetalleBoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

// los detalles de boleta se crean solos al generar la boleta, por eso aqui solo hay consultas
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/detalles-boleta")
@RestController
@Tag(name = "Detalles de boleta", description = "Productos comprados en una boleta")
public class DetalleBoletaController {

    @Autowired
    private DetalleBoletaService detalleBoletaService;

    @Operation(summary = "Lista los productos de una boleta")
    @GetMapping("/boleta/{idBoleta}")
    public ResponseEntity<List<DetalleBoleta>> listarPorBoleta(@PathVariable int idBoleta) {
        return ResponseEntity.ok(detalleBoletaService.listarPorBoleta(idBoleta));
    }

    @Operation(summary = "Obtiene un detalle de boleta por su id")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleBoleta> buscarPorId(@PathVariable int id) {
        DetalleBoleta detalle = detalleBoletaService.buscarPorId(id);
        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalle);
    }

    @Operation(summary = "Calcula el total de una boleta a partir de sus detalles")
    @GetMapping("/boleta/{idBoleta}/total")
    public ResponseEntity<Map<String, Double>> calcularTotal(@PathVariable int idBoleta) {
        return ResponseEntity.ok(Map.of("total", detalleBoletaService.calcularTotal(idBoleta)));
    }

    @Operation(summary = "Cuenta las unidades compradas en una boleta")
    @GetMapping("/boleta/{idBoleta}/unidades")
    public ResponseEntity<Map<String, Integer>> contarUnidades(@PathVariable int idBoleta) {
        return ResponseEntity.ok(Map.of("unidades", detalleBoletaService.contarUnidades(idBoleta)));
    }
}
