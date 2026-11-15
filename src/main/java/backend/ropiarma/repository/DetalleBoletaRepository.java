package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import backend.ropiarma.models.entities.DetalleBoleta;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Integer> {

     List<DetalleBoleta> findByBoletaIdBoleta(int idBoleta);
    
}
