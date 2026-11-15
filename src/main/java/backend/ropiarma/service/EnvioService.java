package backend.ropiarma.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.ropiarma.models.entities.Boleta;
import backend.ropiarma.models.entities.Envio;
import backend.ropiarma.repository.BoletaRepository;
import backend.ropiarma.repository.EnvioRepository;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private BoletaRepository boletaRepository;

    public List<Envio> listar() {
        return envioRepository.findAll();
    }

    // devuelve null si no existe
    public Envio buscarPorId(int id) {
        return envioRepository.findById(id).orElse(null);
    }

    // devuelve null si no existe
    public Envio buscarPorCodigo(String codigoSeguimiento) {
        return envioRepository.findByCodigoSeguimiento(codigoSeguimiento).orElse(null);
    }

    public List<Envio> listarPorBoleta(int idBoleta) {
        return envioRepository.findByBoletaIdBoleta(idBoleta);
    }

    // crea el envio en estado PREPARANDO con un codigo de seguimiento generado
    // devuelve null si la boleta no existe, no esta PAGADA o ya tiene un envio
    public Envio crear(int idBoleta) {
        Boleta boleta = boletaRepository.findById(idBoleta).orElse(null);
        if (boleta == null || !"PAGADA".equals(boleta.getEstado())) {
            return null;
        }
        if (!envioRepository.findByBoletaIdBoleta(idBoleta).isEmpty()) {
            return null;
        }
        Envio envio = new Envio();
        envio.setBoleta(boleta);
        envio.setCodigoSeguimiento(generarCodigo());
        envio.setEstadoEnvio("PREPARANDO");
        envio.setFechaActualizacion(LocalDate.now());
        return envioRepository.save(envio);
    }

    // estados validos: PREPARANDO, ENVIADO, ENTREGADO
    // devuelve null si no existe o el estado no es valido
    public Envio actualizarEstado(int idEnvio, String estado) {
        Envio envio = envioRepository.findById(idEnvio).orElse(null);
        if (envio == null || !esEstadoValido(estado)) {
            return null;
        }
        envio.setEstadoEnvio(estado);
        envio.setFechaActualizacion(LocalDate.now());
        return envioRepository.save(envio);
    }

    private boolean esEstadoValido(String estado) {
        return "PREPARANDO".equals(estado)
                || "ENVIADO".equals(estado)
                || "ENTREGADO".equals(estado);
    }

    private String generarCodigo() {
        String codigo;
        do {
            codigo = "ROP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (envioRepository.findByCodigoSeguimiento(codigo).isPresent());
        return codigo;
    }
}