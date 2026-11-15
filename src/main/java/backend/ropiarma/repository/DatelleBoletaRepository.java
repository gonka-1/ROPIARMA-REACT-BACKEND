package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import backend.ropiarma.models.entities.DetalleBoleta;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface DatelleBoletaRepository extends JpaRepository<DetalleBoleta, Integer> {
    
}
