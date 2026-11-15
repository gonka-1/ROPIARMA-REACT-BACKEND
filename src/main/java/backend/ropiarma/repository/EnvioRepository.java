package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import backend.ropiarma.models.entities.Envio;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface EnvioRepository extends JpaRepository <Envio, Integer> {


    Optional<Envio> findByCodigoSeguimiento(String codigoSeguimiento);

    List<Envio> findByBoletaIdBoleta(int idBoleta);
    
}
