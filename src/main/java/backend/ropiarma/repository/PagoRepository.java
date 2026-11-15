package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import backend.ropiarma.models.entities.Pago;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    
}
