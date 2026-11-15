package backend.ropiarma.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import backend.ropiarma.models.entities.Pago;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByBoletaIdBoleta(int idBoleta);

    boolean existsByBoletaIdBoletaAndEstado(int idBoleta, String estado);
    
}
