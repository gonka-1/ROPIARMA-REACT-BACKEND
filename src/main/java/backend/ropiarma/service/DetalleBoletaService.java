package backend.ropiarma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.ropiarma.models.entities.DetalleBoleta;
import backend.ropiarma.repository.DetalleBoletaRepository;

@Service
public class DetalleBoletaService {

    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;

    public List<DetalleBoleta> listarPorBoleta(int idBoleta) {
        return detalleBoletaRepository.findByBoletaIdBoleta(idBoleta);
    }

    // devuelve null si no existe
    public DetalleBoleta buscarPorId(int id) {
        return detalleBoletaRepository.findById(id).orElse(null);
    }

    public double calcularTotal(int idBoleta) {
        double total = 0;
        for (DetalleBoleta d : detalleBoletaRepository.findByBoletaIdBoleta(idBoleta)) {
            total += d.getCantidad() * d.getPrecioUnitario();
        }
        return total;
    }

    // cantidad total de unidades compradas en la boleta
    public int contarUnidades(int idBoleta) {
        int unidades = 0;
        for (DetalleBoleta d : detalleBoletaRepository.findByBoletaIdBoleta(idBoleta)) {
            unidades += d.getCantidad();
        }
        return unidades;
    }
}