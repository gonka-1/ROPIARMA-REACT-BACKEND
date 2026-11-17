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

import backend.ropiarma.models.entities.Envio;
import backend.ropiarma.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/envios")
@RestController
@Tag(name = "Envios", description = "Envios y seguimiento de boletas pagadas")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Operation(summary = "Lista todos los envios")
    @GetMapping
    public ResponseEntity<List<Envio>> listar() {
        return ResponseEntity.ok(envioService.listar());
    }

    @Operation(summary = "Obtiene un envio por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Envio> buscarPorId(@PathVariable int id) {
        Envio envio = envioService.buscarPorId(id);
        if (envio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(envio);
    }

    @Operation(summary = "Busca un envio por su codigo de seguimiento")
    @GetMapping("/seguimiento/{codigo}")
    public ResponseEntity<Envio> buscarPorCodigo(@PathVariable String codigo) {
        Envio envio = envioService.buscarPorCodigo(codigo);
        if (envio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(envio);
    }

    @Operation(summary = "Lista los envios de una boleta")
    @GetMapping("/boleta/{idBoleta}")
    public ResponseEntity<List<Envio>> listarPorBoleta(@PathVariable int idBoleta) {
        return ResponseEntity.ok(envioService.listarPorBoleta(idBoleta));
    }

    @Operation(summary = "Crea el envio de una boleta PAGADA")
    @PostMapping("/boleta/{idBoleta}")
    public ResponseEntity<?> crear(@PathVariable int idBoleta) {
        Envio envio = envioService.crear(idBoleta);
        if (envio == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "La boleta no existe, no esta PAGADA o ya tiene un envio"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(envio);
    }

    // ejemplo: PUT /api/v1/envios/1/estado?estado=ENVIADO
    @Operation(summary = "Actualiza el estado del envio (PREPARANDO, ENVIADO, ENTREGADO)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable int id, @RequestParam String estado) {
        if (envioService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Envio envio = envioService.actualizarEstado(id, estado);
        if (envio == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Estado invalido"));
        }
        return ResponseEntity.ok(envio);
    }
}
