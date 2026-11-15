package backend.ropiarma.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ropiarma.models.entities.Boleta;
import backend.ropiarma.models.entities.Pago;
import backend.ropiarma.repository.BoletaRepository;
import backend.ropiarma.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private BoletaRepository boletaRepository;

    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    // devuelve null si no existe
    public Pago buscarPorId(int id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public List<Pago> listarPorBoleta(int idBoleta) {
        return pagoRepository.findByBoletaIdBoleta(idBoleta);
    }

    // registra un pago PENDIENTE por el total de la boleta
    // devuelve null si la boleta no existe, esta cancelada o ya tiene un pago aprobado
    public Pago registrar(int idBoleta, String metodoPago) {
        Boleta boleta = boletaRepository.findById(idBoleta).orElse(null);
        if (boleta == null || "CANCELADA".equals(boleta.getEstado())) {
            return null;
        }
        if (pagoRepository.existsByBoletaIdBoletaAndEstado(idBoleta, "APROBADO")) {
            return null;
        }
        Pago pago = new Pago();
        pago.setBoleta(boleta);
        pago.setMonto(boleta.getTotal());
        pago.setMetodoPago(metodoPago);
        pago.setEstado("PENDIENTE");
        pago.setFechaPago(LocalDate.now());
        return pagoRepository.save(pago);
    }

    // aprueba el pago y marca la boleta como PAGADA
    // devuelve null si no existe o no esta PENDIENTE
    @Transactional
    public Pago aprobar(int idPago) {
        Pago pago = pagoRepository.findById(idPago).orElse(null);
        if (pago == null || !"PENDIENTE".equals(pago.getEstado())) {
            return null;
        }
        pago.setEstado("APROBADO");
        pago.setFechaPago(LocalDate.now());

        Boleta boleta = pago.getBoleta();
        boleta.setEstado("PAGADA");
        boletaRepository.save(boleta);

        return pagoRepository.save(pago);
    }

    // devuelve null si no existe o no esta PENDIENTE
    public Pago rechazar(int idPago) {
        Pago pago = pagoRepository.findById(idPago).orElse(null);
        if (pago == null || !"PENDIENTE".equals(pago.getEstado())) {
            return null;
        }
        pago.setEstado("RECHAZADO");
        return pagoRepository.save(pago);
    }
}