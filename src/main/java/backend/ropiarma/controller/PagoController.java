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

import backend.ropiarma.models.entities.Pago;
import backend.ropiarma.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/pagos")
@RestController
@Tag(name = "Pagos", description = "Pagos asociados a una boleta")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Lista todos los pagos")
    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(pagoService.listar());
    }

    @Operation(summary = "Obtiene un pago por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable int id) {
        Pago pago = pagoService.buscarPorId(id);
        if (pago == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pago);
    }

    @Operation(summary = "Lista los pagos de una boleta")
    @GetMapping("/boleta/{idBoleta}")
    public ResponseEntity<List<Pago>> listarPorBoleta(@PathVariable int idBoleta) {
        return ResponseEntity.ok(pagoService.listarPorBoleta(idBoleta));
    }

    // ejemplo: POST /api/v1/pagos/boleta/1?metodoPago=DEBITO
    @Operation(summary = "Registra un pago PENDIENTE por el total de la boleta")
    @PostMapping("/boleta/{idBoleta}")
    public ResponseEntity<?> registrar(@PathVariable int idBoleta, @RequestParam String metodoPago) {
        Pago pago = pagoService.registrar(idBoleta, metodoPago);
        if (pago == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "La boleta no existe, esta cancelada o ya tiene un pago aprobado"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }

    @Operation(summary = "Aprueba un pago y marca la boleta como PAGADA")
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobar(@PathVariable int id) {
        if (pagoService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Pago pago = pagoService.aprobar(id);
        if (pago == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El pago no esta PENDIENTE"));
        }
        return ResponseEntity.ok(pago);
    }

    @Operation(summary = "Rechaza un pago")
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable int id) {
        if (pagoService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Pago pago = pagoService.rechazar(id);
        if (pago == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El pago no esta PENDIENTE"));
        }
        return ResponseEntity.ok(pago);
    }
}
